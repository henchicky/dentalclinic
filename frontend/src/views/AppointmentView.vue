<template>
  <div class="appointment-form-bg">
    <div class="appointment-form-center">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>Schedule Appointment</span>
          </div>
        </template>

        <el-form :model="appointmentForm" label-width="150px">
          <el-form-item label="Name" required>
            <el-input
              v-model="appointmentForm.patientName"
              placeholder="Enter name"
              :prefix-icon="User"
            />
          </el-form-item>

          <el-form-item label="Appointment Type" required>
            <el-select v-model="appointmentForm.appointmentType" placeholder="Select appointment type">
              <el-option
                v-for="type in appointmentTypes"
                :key="type.id"
                :label="type.name"
                :value="type.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="Appointment" required>
            <el-row style="width: 100%">
              <el-col :span="12" style="padding-right: 10px;">
                <el-date-picker
                  v-model="appointmentForm.appointmentDate"
                  type="date"
                  placeholder="Select date"
                  :disabled-date="disabledDate"
                  style="width: 100%"
                />
              </el-col>
              <el-col :span="12">
                <el-time-select
                  v-model="appointmentForm.appointmentTime"
                  placeholder="Select time"
                  :picker-options="timeOptions"
                  style="width: 100%"
                />
              </el-col>
            </el-row>
          </el-form-item>

          <el-form-item label="Description">
            <el-input
              v-model="appointmentForm.description"
              type="textarea"
              :rows="4"
              placeholder="Enter appointment description"
            />
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
import { reactive, onMounted } from 'vue'
import { User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

interface AppointmentType {
  id: number;
  name: string;
}

const appointmentTypes = reactive<AppointmentType[]>([]);

onMounted(async () => {
  try {
    const response = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/appointments/appointmentTypes`);
    appointmentTypes.push(...response.data);
  } catch (error) {
    ElMessage.error('Failed to load appointment types!');
  }
});

interface AppointmentForm {
  patientName: string;
  appointmentDate: Date | null;
  appointmentTime: string | null;
  description: string;
  appointmentType: number | null;
}

const appointmentForm = reactive<AppointmentForm>({
  patientName: '',
  appointmentDate: null,
  appointmentTime: null,
  description: '',
  appointmentType: null,
})

// Disable past dates
const disabledDate = (time: Date) => {
  const endOfToday = new Date()
  endOfToday.setHours(23, 59, 59, 999)
  return time.getTime() < endOfToday.getTime()
}

const submitAppointment = async () => {
  if (
    !appointmentForm.patientName ||
    !appointmentForm.appointmentDate ||
    !appointmentForm.appointmentTime
  ) {
    ElMessage.error('Please fill in all required fields')
    return
  }

  // Combine date and time
  const appointmentDateTime = new Date(appointmentForm.appointmentDate)
  const [hours, minutes] = appointmentForm.appointmentTime.split(':').map(Number)
  appointmentDateTime.setHours(hours)
  appointmentDateTime.setMinutes(minutes)

  try {
    await axios.post(`${import.meta.env.VITE_API_BASE_URL}/appointments`, {
      patientName: appointmentForm.patientName,
      appointmentTime: appointmentDateTime,
      description: appointmentForm.description,
    })
    ElMessage.success('Appointment scheduled successfully!')
    resetForm()
  } catch (error) {
    ElMessage.error('Failed to schedule appointment!')
  }
}

const resetForm = () => {
  appointmentForm.patientName = ''
  appointmentForm.appointmentDate = null
  appointmentForm.appointmentTime = null
  appointmentForm.description = ''
  appointmentForm.appointmentType = null
}

//TODO: get available time options from db
const timeOptions = {
  start: '09:00',
  end: '17:00',
  step: '00:30',
}
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
