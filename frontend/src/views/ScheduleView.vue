<template>
  <div class="schedule-container">
    <div class="schedule-header">
      <div>
        <h2>{{ username }}'s Schedule</h2>
        <p class="subtitle">Your appointments for {{ formattedDate }}</p>
      </div>
      <el-date-picker
        v-model="selectedDate"
        type="date"
        placeholder="Select a date"
        :clearable="false"
      />
    </div>

    <div class="schedule-grid">
      <div class="timeline">
        <div class="hour-marker" v-for="hour in 13" :key="hour">
          {{ formatHour(hour + 7) }}
        </div>
      </div>
      
      <div class="appointments-container">
        <div class="current-time-line" :style="currentTimeStyle"></div>
        
        <div v-if="appointments.length === 0" class="no-appointments">
          <el-empty description="No appointments found" />
        </div>
        
        <div v-else class="appointment-cards">
          <div
            v-for="appointment in appointments"
            :key="appointment.id"
            class="appointment-card"
            :style="getAppointmentStyle(appointment)"
          >
            <div class="appointment-content">
              <div class="appointment-header">
                {{ appointment.appointmentType.name }} · {{ appointment.appointmentType.durationMinutes }}min
              </div>
              <div class="appointment-details">
                {{ formatTime(appointment.appointmentTime) }} · {{ appointment.patient.name }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { useAuthStore } from '../stores/auth'
import { ElDatePicker, ElEmpty } from 'element-plus'
import axios from 'axios'
import type { Appointment } from '@/types/Appointment'
import { offsetDate } from '@/helper'

const authStore = useAuthStore()

const selectedDate = ref(new Date())
const username = computed(() => authStore.user)
const appointments = ref([] as Appointment[])

const formattedDate = computed(() =>
  selectedDate.value
    ? new Date(selectedDate.value).toLocaleDateString(undefined, {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      })
    : '',
)

const fetchAppointments = async () => {
  const userId = localStorage.getItem('userId')
  if (!userId || !selectedDate.value) {
    appointments.value = []
    return
  }

  axios
    .get<Appointment[]>(`${import.meta.env.VITE_API_BASE_URL}/dentists/${userId}/appointments`, {
      params: {
        date: offsetDate(selectedDate.value).toISOString().split('T')[0],
      },
    })
    .then((response) => {
      appointments.value = response.data
    })
    .catch((error) => {
      console.error('Error fetching appointments:', error)
      appointments.value = []
    })
}

watch(selectedDate, fetchAppointments)

onMounted(fetchAppointments)

const formatHour = (hour: number) => {
  return `${hour.toString().padStart(2, '0')}:00`
}

const formatTime = (time: Date) => {
  return new Date(time).toLocaleTimeString('en-US', { 
    hour: '2-digit', 
    minute: '2-digit',
    hour12: false 
  })
}

const currentTimeStyle = computed(() => {
  const now = new Date()
  const minutes = now.getHours() * 60 + now.getMinutes()
  const startOfDay = 8 * 60 // 8 AM in minutes
  const totalMinutes = 12 * 60 // 12 hours in minutes
  const percentage = ((minutes - startOfDay) / totalMinutes) * 100
  
  // Only show the current time line between 8 AM and 8 PM
  if (minutes < startOfDay || minutes > startOfDay + totalMinutes) {
    return { display: 'none' }
  }
  
  return {
    top: `${percentage}%`
  }
})

const getAppointmentStyle = (appointment: Appointment) => {
  const startTime = new Date(appointment.appointmentTime)
  const minutes = startTime.getHours() * 60 + startTime.getMinutes()
  const duration = appointment.appointmentType.durationMinutes
  // Adjust calculations to be based on 12-hour window (8am-8pm = 720 minutes)
  const startOfDay = 8 * 60 // 8 AM in minutes
  const totalMinutes = 12 * 60 // 12 hours in minutes
  const topPercentage = ((minutes - startOfDay) / totalMinutes) * 100
  const heightPercentage = (duration / totalMinutes) * 100
  
  return {
    top: `${topPercentage}%`,
    height: `${heightPercentage}%`
  }
}
</script>

<style scoped>
.schedule-container {
  padding: 1.2rem 0.5rem;
  max-width: 1000px;
  margin: 20px auto;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.schedule-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 3rem;
  border-bottom: 1px solid #f0f0f0;
  padding: 1rem;
}

.subtitle {
  color: #888;
  font-size: 0.95rem;
  margin-top: 0.15rem;
  margin-bottom: 0rem;
}

.schedule-grid {
  display: flex;
  height: 720px;
  position: relative;
  margin: 0 1rem;
}

.timeline {
  width: 60px;
  border-right: 1px solid #eee;
  position: relative;
}

.hour-marker {
  position: absolute;
  font-size: 0.8rem;
  color: #999;
  transform: translateY(-50%);
  padding-right: 8px;
  text-align: right;
  width: 100%;
  height: 2px;
}

.hour-marker::after {
  content: '';
  position: absolute;
  right: -1px;
  top: 50%;
  width: 5px;
  height: 1px;
  background: #eee;
}

.appointments-container {
  flex: 1;
  position: relative;
  margin-left: 1rem;
  overflow: hidden;
}

.current-time-line {
  position: absolute;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #4CAF50;
  z-index: 1;
}

.appointment-cards {
  position: relative;
  height: 100%;
}

.appointment-card {
  position: absolute;
  left: 0;
  right: 0;
  background: #ffffff;
  border-left: 4px solid #4CAF50;
  padding: 8px;
  margin-right: 8px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  z-index: 1;
  transition: transform 0.2s, box-shadow 0.2s;
  margin-left: 4px;
}

.appointment-card:hover {
  transform: translateX(2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.15);
}

.appointment-content {
  background: #ffffff;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.appointment-header {
  font-size: 0.9rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 4px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.appointment-details {
  font-size: 0.85rem;
  color: #666;
  display: flex;
  align-items: center;
  gap: 8px;
}

.no-appointments {
  text-align: center;
  padding: 1.2rem 0;
  color: #aaa;
}

.hour-marker {
  height: calc(100% / 13);
}

.hour-marker:nth-child(1) { top: 0%; }
.hour-marker:nth-child(2) { top: 8.333333333%; }
.hour-marker:nth-child(3) { top: 16.666666667%; }
.hour-marker:nth-child(4) { top: 25%; }
.hour-marker:nth-child(5) { top: 33.333333333%; }
.hour-marker:nth-child(6) { top: 41.666666667%; }
.hour-marker:nth-child(7) { top: 50%; }
.hour-marker:nth-child(8) { top: 58.333333333%; }
.hour-marker:nth-child(9) { top: 66.666666667%; }
.hour-marker:nth-child(10) { top: 75%; }
.hour-marker:nth-child(11) { top: 83.333333333%; }
.hour-marker:nth-child(12) { top: 91.666666667%; }
.hour-marker:nth-child(13) { top: 100%; }
</style>
