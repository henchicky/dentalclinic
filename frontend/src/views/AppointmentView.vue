<template>
  <div class="appointment-form-bg">
    <div class="appointment-form-center">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>Schedule Appointment</span>
          </div>
        </template>

        <el-form :model="appointmentForm" :rules="rules" ref="appointmentFormRef" label-position="top">
          <el-form-item label="Name" prop="patientName" required>
            <el-input v-model="appointmentForm.patientName" placeholder="Enter name" :prefix-icon="User" />
          </el-form-item>

          <el-form-item label="Appointment Type" prop="appointmentType" required>
            <el-select v-model="appointmentForm.appointmentType" placeholder="Select appointment type">
              <el-option v-for="type in appointmentTypes" :key="type.id" :label="type.name" :value="type.id" />
            </el-select>
          </el-form-item>

          <el-form-item label="Appointment" required>
            <el-row style="width: 100%">
              <el-col :span="12" style="padding-right: 10px;">
                <el-form-item prop="appointmentDate">
                  <el-date-picker v-model="appointmentForm.appointmentDate" type="date" placeholder="Select date"
                    :disabled-date="disabledDate" @change="" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item prop="appointmentTime">
                  <el-select v-model="appointmentForm.appointmentTime" placeholder="Select time">
                    <el-option v-for="time in availableTimes" :key="time" :label="time" :value="time" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form-item>

          <el-form-item label="Description">
            <el-input v-model="appointmentForm.description" type="textarea" :rows="4"
              placeholder="Enter appointment description" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitAppointment">Book</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted, ref, computed, watch } from 'vue'
import { User, Clock } from '@element-plus/icons-vue'
import { ElNotification } from 'element-plus'
import axios from 'axios'
import { format } from 'date-fns'

interface AppointmentType {
  id: number;
  name: string;
}

interface AppointmentForm {
  patientName: string;
  appointmentDate: Date | null;
  appointmentTime: string | null;
  description: string;
  appointmentType: number | null;
}

const appointmentTypes = reactive<AppointmentType[]>([]);
const appointmentFormRef = ref();
const availableDates = ref<Record<string, string[]>>({});

const appointmentForm = reactive<AppointmentForm>({
  patientName: '',
  appointmentDate: null,
  appointmentTime: null,
  description: '',
  appointmentType: null,
})

const rules = {
  patientName: [
    { required: true, message: 'Please enter your name', trigger: 'blur' },
  ],
  appointmentType: [
    { required: true, message: 'Please select an appointment type', trigger: 'change' },
  ],
  appointmentDate: [
    { required: true, message: 'Please select a date', trigger: 'change' },
  ],
  appointmentTime: [
    { required: true, message: 'Please select a time', trigger: 'change' },
  ],
};

const availableTimes = computed(() => {
  if (!appointmentForm.appointmentDate) return [];
  offsetDate(appointmentForm.appointmentDate);
  const selectedDate = appointmentForm.appointmentDate.toISOString().split('T')[0];
  const availableTimes = availableDates.value[selectedDate] || [];

  return availableTimes.map((time) => {
    const [hours, minutes] = time.split(':').map(Number);
    const date = new Date();
    date.setHours(hours, minutes);
    return format(date, 'H:mm');
  });
});

const disabledDate = (time: Date) => {
  offsetDate(time);
  const dateString = time.toISOString().split('T')[0];
  return !availableDates.value[dateString];
};

function offsetDate(date: Date) {
  const timezoneOffset = date.getTimezoneOffset()
  date.setMinutes(date.getMinutes() - timezoneOffset);
  return date;
}

const submitAppointment = async () => {
  appointmentFormRef.value.validate(async (valid: any) => {
    if (!valid) {
      ElNotification({
        title: 'Error',
        message: 'Please fill in all required fields',
        type: 'error',
      });
      return;
    }

    // Combine date and time
    const appointmentDateTime = new Date(appointmentForm.appointmentDate ?? new Date());
    const [hours, minutes] = (appointmentForm.appointmentTime ?? '00:00').split(':').map(Number);
   
    appointmentDateTime.setHours(hours, minutes);
    offsetDate(appointmentDateTime)
    appointmentDateTime.setMinutes(minutes);

    console.log('Final Submitting appointment:', appointmentDateTime);

    try {
      await axios.post(`${import.meta.env.VITE_API_BASE_URL}/appointments`, {
        name: appointmentForm.patientName,
        appointmentTime: appointmentDateTime,
        description: appointmentForm.description,
        appointmentType: appointmentForm.appointmentType,
      });
      resetForm();
      fetchAvailableSlots()
      ElNotification({
        title: 'Success',
        message: 'Appointment scheduled successfully!',
        type: 'success',
      });
    } catch (error: any) {
      console.log(error);
      ElNotification({
        title: 'Error',
        message: error?.response?.data?.message || 'Failed to schedule appointment!',
        type: 'error',
      });
    }
  });
}

const resetForm = () => {
  appointmentForm.patientName = ''
  appointmentForm.appointmentDate = null
  appointmentForm.appointmentTime = null
  appointmentForm.description = ''
  appointmentForm.appointmentType = null
  appointmentFormRef.value.resetFields()
}

function fetchAppointmentTypes(){
  axios.get(`${import.meta.env.VITE_API_BASE_URL}/appointments/appointmentTypes`)
    .then((response) => {
      appointmentTypes.push(...response.data);
    })
    .catch((error) => {
      console.error('Error fetching appointment types:', error);
      ElNotification({
        title: 'Error',
        message: 'Failed to load appointment types!',
        type: 'error',
      })
    });
}

function fetchAvailableSlots() {
  axios.get(`${import.meta.env.VITE_API_BASE_URL}/appointments/availableSlots`)
    .then((response) => {
      response.data.forEach((slot: { date: string, availableTimings: string[] }) => {
        availableDates.value[slot.date] = slot.availableTimings;
      });
    })
    .catch((error) => {
      console.error('Error fetching available slots:', error);
      ElNotification({
        title: 'Error',
        message: 'Failed to load available slots!',
        type: 'error',
      });
    });
}
onMounted(() => {
  fetchAppointmentTypes()
  fetchAvailableSlots()
});
</script>

<style scoped>
.appointment-form-bg {
  background: transparent;
  height: 91vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.appointment-form-center {
  width: 100%;
  max-width: 800px;
  margin: 2rem auto;
  display: flex;
  align-items: center;
  justify-content: center;
}

.box-card {
  width: 100%;
  border-radius: 16px;
  box-shadow:
    0 4px 24px rgba(74, 144, 226, 0.08),
    0 1.5px 6px rgba(44, 62, 80, 0.04);
  padding: 2rem 1.5rem;
  background: #fff;
}

.card-header {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.4rem;
  font-weight: 700;
  color: #357abd;
  letter-spacing: 1px;
  margin-bottom: 1rem;
}

:deep(.el-date-editor.el-input),
:deep(.el-date-editor.el-input__wrapper) {
  width: 100%;
}
</style>
