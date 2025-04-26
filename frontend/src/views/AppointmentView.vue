<template>
  <div class="appointment-form-bg">
    <div class="appointment-form-center">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>Schedule Appointment</span>
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
                  :disabled-date="disabledDate"
                  @change="handleDateChange"
                  style="width: 200px"
                />
              </el-col>
              <el-col :span="12">
                <el-time-select
                  v-model="appointmentForm.appointmentTime"
                  placeholder="Select time"
                  :picker-options="timeOptions"
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

// Disable past dates
const disabledDate = (time: Date) => {
  const endOfToday = new Date()
  endOfToday.setHours(23, 59, 59, 999)
  return time.getTime() < endOfToday.getTime()
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