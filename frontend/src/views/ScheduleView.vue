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
        <div v-if="filteredAppointments.length === 0" class="no-appointments">
          No appointments found
        </div>
        <div v-else v-for="appointment in filteredAppointments" :key="appointment.id" class="appointment-item">
          <div class="appointment-time">
            {{ formatTime(appointment.startTime) }} - {{ formatTime(appointment.endTime) }}
          </div>
          <div class="appointment-details">
            <h3>{{ appointment.patientName }}</h3>
            <p>{{ appointment.procedure }}</p>
          </div>
          <div class="appointment-status" :class="appointment.status">
            {{ appointment.status }}
          </div>
        </div>
      </div>
    </div>

    <!-- Appointment Details Modal -->
    <div v-if="selectedAppointment" class="modal">
      <div class="modal-content">
        <h2>Appointment Details</h2>
        <div class="appointment-info">
          <p><strong>Patient:</strong> {{ selectedAppointment.patientName }}</p>
          <p><strong>Date:</strong> {{ formatDate(selectedAppointment.date) }}</p>
          <p>
            <strong>Time:</strong> {{ formatTime(selectedAppointment.startTime) }} -
            {{ formatTime(selectedAppointment.endTime) }}
          </p>
          <p><strong>Procedure:</strong> {{ selectedAppointment.procedure }}</p>
          <p><strong>Status:</strong> {{ selectedAppointment.status }}</p>
        </div>
        <button @click="selectedAppointment = null">Close</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useAuthStore } from '../stores/auth'
import { ElDatePicker } from 'element-plus'

const authStore = useAuthStore()

const selectedDate = ref(new Date())
const selectedAppointment = ref(null)
const username = computed(() => authStore.user)

// Mock data - replace with actual API calls
const appointments = ref([
  {
    id: 1,
    patientName: 'John Doe',
    date: '2024-03-15',
    startTime: '09:00',
    endTime: '10:00',
    procedure: 'Regular Checkup',
    status: 'confirmed',
  },
  {
    id: 2,
    patientName: 'Jane Smith',
    date: '2024-03-15',
    startTime: '14:00',
    endTime: '15:00',
    procedure: 'Teeth Cleaning',
    status: 'pending',
  },
])

const filteredAppointments = computed(() => {
  console.log('Filtering appointments for date:', selectedDate.value)
  if (!selectedDate.value) {
    return appointments.value
  }
  return appointments.value.filter((a) => a.date === selectedDate.value)
})

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
