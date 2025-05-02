<template>
  <div class="schedule-container">
    <div class="schedule-header">
      <h1>{{ username }}'s Schedule</h1>
    </div>

    <div class="list-view">
      <div class="appointment-filters">
        <el-date-picker v-model="selectedDate" type="date" placeholder="Select a date" />
      </div>
      <div class="appointment-list">
        <div v-if="appointments.length === 0" class="no-appointments">
          No appointments found
        </div>
        <div v-else v-for="appointment in appointments" :key="appointment.id" class="appointment-item">
          <div class="appointment-time">
            {{ appointment.appointmentTime }} - {{ appointment.appointmentEndTime }}
          </div>
          <div class="appointment-details">
            <h3>{{ appointment.patient.name }}</h3>
            <p>{{ appointment.appointmentType.name }}</p>
            <p>{{ appointment.description }}</p>
          </div>
          <div class="appointment-status">
            {{ appointment.appointmentStatus }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { useAuthStore } from '../stores/auth'
import { ElDatePicker } from 'element-plus'
import axios from 'axios'
import type { Appointment } from '@/types/Appointment'

const authStore = useAuthStore()

const selectedDate = ref(new Date())
const username = computed(() => authStore.user)
const appointments = ref([] as Appointment[])

const fetchAppointments = async () => {
  const userId = localStorage.getItem('userId')
  if (!userId || !selectedDate.value) {
    appointments.value = []
    return
  }

  axios.get<Appointment[]>(`${import.meta.env.VITE_API_BASE_URL}/dentists/${userId}/appointments`, {
    params: {
      date: selectedDate.value.toISOString().split('T')[0],
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

// Watch for changes in selectedDate and fetch appointments
watch(selectedDate, fetchAppointments)

onMounted(fetchAppointments)

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}

const formatTime = (time: string) => {
  return new Date(`2000-01-01T${time}`).toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
  })
}
</script>

<style scoped>
.schedule-container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.schedule-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.list-view {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 1rem;
}

.appointment-filters {
  margin-bottom: 1rem;
}

.date-picker {
  width: 200px;
}

.appointment-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.appointment-item {
  display: flex;
  align-items: center;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.appointment-time {
  width: 150px;
  font-weight: bold;
}

.appointment-details {
  flex: 1;
}

.appointment-status {
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
}

.no-appointments {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  max-width: 500px;
  width: 90%;
}

.appointment-info {
  margin: 1rem 0;
}

.appointment-info p {
  margin: 0.5rem 0;
}
</style>
