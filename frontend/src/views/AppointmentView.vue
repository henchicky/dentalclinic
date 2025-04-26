<template>
  <div class="appointment-form-bg">
    <div class="appointment-form-center">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>Schedule New Appointment</span>
          </div>
        </template>
        
        <el-form :model="appointmentForm" label-width="120px">
          <el-form-item label="Patient Name" required>
            <el-input 
              v-model="appointmentForm.patientName" 
              placeholder="Enter patient name"
              :prefix-icon="User"
            />
          </el-form-item>

          <el-form-item label="Appointment" required>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-date-picker
                  v-model="appointmentForm.appointmentDate"
                  type="date"
                  placeholder="Select date"
                  :shortcuts="dateShortcuts"
                  :disabled-date="disabledDate"
                  @change="handleDateChange"
                  style="width: 200px"
                />
              </el-col>
              <el-col :span="12">
                <el-time-picker
                  v-model="appointmentForm.appointmentTime"
                  placeholder="Select time"
                  format="HH:mm"
                  :disabled-hours="disabledHours"
                  :disabled-minutes="disabledMinutes"
                  style="width: 200px"
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
import { ref, reactive } from 'vue'
import { User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

interface AppointmentForm {
  patientName: string
  appointmentDate: Date | null
  appointmentTime: Date | null
  description: string
}

const appointmentForm = reactive<AppointmentForm>({
  patientName: '',
  appointmentDate: null,
  appointmentTime: null,
  description: ''
})

const dateShortcuts = [
  {
    text: 'Today',
    value: new Date(),
  },
  {
    text: 'Tomorrow',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24)
      return date
    },
  },
]

// Disable past dates
const disabledDate = (time: Date) => {
  return time.getTime() < Date.now() - 8.64e7
}

// Disable hours outside business hours (9 AM - 5 PM)
const disabledHours = () => {
  const hours = []
  for (let i = 0; i < 24; i++) {
    if (i < 9 || i >= 17) {
      hours.push(i)
    }
  }
  return hours
}

// Only allow appointments on the hour or half hour
const disabledMinutes = (hour: number) => {
  const minutes = []
  for (let i = 0; i < 60; i++) {
    if (i % 30 !== 0) {
      minutes.push(i)
    }
  }
  return minutes
}

// Disable seconds selection
const disabledSeconds = () => {
  return [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59]
}

const handleDateChange = (date: Date) => {
  // If the selected date is today, disable past hours
  const now = new Date()
  if (date && date.toDateString() === now.toDateString()) {
    const currentHour = now.getHours()
    const currentMinutes = now.getMinutes()
    
    // If current time is past business hours, disable time picker
    if (currentHour >= 17) {
      appointmentForm.appointmentTime = null
      ElMessage.warning('Business hours are over for today')
    }
  }
}

const submitAppointment = () => {
  if (!appointmentForm.patientName || !appointmentForm.appointmentDate || !appointmentForm.appointmentTime) {
    ElMessage.error('Please fill in all required fields')
    return
  }

  // Combine date and time
  const appointmentDateTime = new Date(appointmentForm.appointmentDate)
  const time = appointmentForm.appointmentTime
  appointmentDateTime.setHours(time.getHours())
  appointmentDateTime.setMinutes(time.getMinutes())

  // TODO: Add API call to backend
  console.log('Submitting appointment:', {
    patientName: appointmentForm.patientName,
    appointmentTime: appointmentDateTime,
    description: appointmentForm.description
  })
  
  ElMessage.success('Appointment scheduled successfully!')
  resetForm()
}

const resetForm = () => {
  appointmentForm.patientName = ''
  appointmentForm.appointmentDate = null
  appointmentForm.appointmentTime = null
  appointmentForm.description = ''
}
</script>

<style scoped>
.appointment-form-bg {
  min-height: 100vh;
  background: linear-gradient(135deg, #e3f0ff 0%, #f8f9fa 100%);
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
  box-shadow: 0 4px 24px rgba(74, 144, 226, 0.08), 0 1.5px 6px rgba(44, 62, 80, 0.04);
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

.el-form-item {
  margin-bottom: 1.5rem;
}

.el-button {
  min-width: 140px;
}

:deep(.el-date-editor.el-input),
:deep(.el-date-editor.el-input__wrapper) {
  width: 100%;
}
</style> 