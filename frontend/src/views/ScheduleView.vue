<template>
  <div class="schedule-container">
    <div class="schedule-header">
      <h1>{{ username }}'s Schedule</h1>
      <div class="view-toggle">
        <button :class="{ active: view === 'calendar' }" @click="view = 'calendar'">
          Calendar
        </button>
        <button :class="{ active: view === 'list' }" @click="view = 'list'">List</button>
      </div>
    </div>

    <!-- Calendar View -->
    <div v-if="view === 'calendar'" class="calendar-view">
      <div class="calendar-header">
        <button @click="prevMonth">&lt;</button>
        <h2>{{ currentMonthYear }}</h2>
        <button @click="nextMonth">&gt;</button>
      </div>
      <div class="calendar-grid">
        <div class="calendar-weekday" v-for="day in weekdays" :key="day">
          {{ day }}
        </div>
        <div
          v-for="day in calendarDays"
          :key="day.date"
          class="calendar-day"
          :class="{
            'current-month': day.isCurrentMonth,
            today: isToday(day.date),
            'has-appointments': hasAppointments(day.date),
          }"
          @click="selectDate(day.date)"
        >
          <span class="day-number">{{ day.day }}</span>
          <div v-if="hasAppointments(day.date)" class="appointment-indicator"></div>
        </div>
      </div>
    </div>

    <!-- List View -->
    <div v-else class="list-view">
      <div class="appointment-filters">
        <select v-model="selectedDate">
          <option value="all">All Dates</option>
          <option v-for="date in uniqueDates" :key="date" :value="date">
            {{ formatDate(date) }}
          </option>
        </select>
      </div>
      <div class="appointment-list">
        <div v-if="filteredAppointments.length === 0" class="no-appointments">
          No appointments found
        </div>
        <div
          v-else
          v-for="appointment in filteredAppointments"
          :key="appointment.id"
          class="appointment-item"
        >
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

const authStore = useAuthStore()

const view = ref<'calendar' | 'list'>('calendar')
const currentDate = ref(new Date())
const selectedDate = ref('all')
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

const weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']

const currentMonthYear = computed(() => {
  return currentDate.value.toLocaleDateString('en-US', {
    month: 'long',
    year: 'numeric',
  })
})

const calendarDays = computed(() => {
  const year = currentDate.value.getFullYear()
  const month = currentDate.value.getMonth()

  const firstDay = new Date(year, month, 1)
  const lastDay = new Date(year, month + 1, 0)

  const days = []
  const startingDay = firstDay.getDay()

  // Add days from previous month
  for (let i = startingDay - 1; i >= 0; i--) {
    const date = new Date(year, month, -i)
    days.push({
      date: date.toISOString().split('T')[0],
      day: date.getDate(),
      isCurrentMonth: false,
    })
  }

  // Add days from current month
  for (let i = 1; i <= lastDay.getDate(); i++) {
    const date = new Date(year, month, i)
    days.push({
      date: date.toISOString().split('T')[0],
      day: i,
      isCurrentMonth: true,
    })
  }

  // Add days from next month
  const remainingDays = 42 - days.length // 6 rows * 7 days
  for (let i = 1; i <= remainingDays; i++) {
    const date = new Date(year, month + 1, i)
    days.push({
      date: date.toISOString().split('T')[0],
      day: i,
      isCurrentMonth: false,
    })
  }

  return days
})

const uniqueDates = computed(() => {
  return [...new Set(appointments.value.map((a) => a.date))].sort()
})

const filteredAppointments = computed(() => {
  if (selectedDate.value === 'all') {
    return appointments.value
  }
  return appointments.value.filter((a) => a.date === selectedDate.value)
})

const prevMonth = () => {
  currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() - 1, 1)
}

const nextMonth = () => {
  currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() + 1, 1)
}

const isToday = (date: string) => {
  const today = new Date().toISOString().split('T')[0]
  return date === today
}

const hasAppointments = (date: string) => {
  return appointments.value.some((a) => a.date === date)
}

const selectDate = (date: string) => {
  if (hasAppointments(date)) {
    view.value = 'list'
    selectedDate.value = date
  }
}

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

.view-toggle {
  display: flex;
  gap: 1rem;
}

.view-toggle button {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
  border-radius: 4px;
}

.view-toggle button.active {
  background: #4a90e2;
  color: white;
  border-color: #4a90e2;
}

.calendar-view {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 1rem;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.calendar-header button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 0.5rem;
}

.calendar-weekday {
  text-align: center;
  font-weight: bold;
  padding: 0.5rem;
}

.calendar-day {
  aspect-ratio: 1;
  padding: 0.5rem;
  border: 1px solid #ddd;
  cursor: pointer;
  position: relative;
}

.calendar-day.current-month {
  background: white;
}

.calendar-day:not(.current-month) {
  background: #f5f5f5;
  color: #999;
}

.calendar-day.today {
  border-color: #4a90e2;
}

.calendar-day.has-appointments .appointment-indicator {
  position: absolute;
  bottom: 0.5rem;
  left: 50%;
  transform: translateX(-50%);
  width: 6px;
  height: 6px;
  background: #4a90e2;
  border-radius: 50%;
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

.appointment-filters select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
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

.appointment-status.confirmed {
  background: #d4edda;
  color: #155724;
}

.appointment-status.pending {
  background: #fff3cd;
  color: #856404;
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
