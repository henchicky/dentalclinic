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
        class="date-picker"
        :clearable="false"
      />
    </div>

    <div class="list-view">
      <div v-if="appointments.length === 0" class="no-appointments">
        <el-empty description="No appointments found" />
      </div>
      <el-timeline v-else class="appointment-timeline">
        <el-timeline-item
          v-for="appointment in appointments"
          :key="appointment.id"
          :timestamp="appointment.appointmentTime.toString()"
          :color="getStatusColor(appointment.appointmentStatus)"
        >
          <el-card>
            <p>Appointment Type: {{ appointment.appointmentType.name }}</p>
            <p>Duration: {{ appointment.appointmentType.durationMinutes }}</p>
            <p>Patient: {{ appointment.patient.name }}</p>
            {{ appointment.description }}
            <div class="appointment-footer">
              <el-tag :type="getStatusTagType(appointment.appointmentStatus)">
                {{ appointment.appointmentStatus }}
              </el-tag>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { useAuthStore } from '../stores/auth'
import { ElDatePicker, ElEmpty, ElCard, ElTag, ElTimeline, ElTimelineItem } from 'element-plus'
import axios from 'axios'
import type { Appointment } from '@/types/Appointment'
import { offsetDate } from '@/helper'
import 'element-plus/es/components/avatar/style/css'
import 'element-plus/es/components/timeline/style/css'
import 'element-plus/es/components/timeline-item/style/css'

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

// Watch for changes in selectedDate and fetch appointments
watch(selectedDate, fetchAppointments)

onMounted(fetchAppointments)

const getStatusTagType = (status: string) => {
  switch (status) {
    case 'CANCELLED':
      return 'danger'
    case 'UPCOMING':
      return 'success'
    case 'COMPLETED':
      return 'info'
    default:
      return 'warning'
  }
}

const getStatusColor = (status: string) => {
  switch (status) {
    case 'CANCELLED':
      return '#f56c6c'
    case 'UPCOMING':
      return '#67c23a'
    case 'COMPLETED':
      return '#409eff'
    default:
      return '#e6a23c'
  }
}

const getTypeTagType = (type: string) => {
  switch (type.toLowerCase()) {
    case 'consultation':
      return 'info'
    case 'surgery':
      return 'danger'
    case 'cleaning':
      return 'success'
    default:
      return 'primary'
  }
}
</script>

<style scoped>
.schedule-container {
  padding: 1.2rem 0.5rem;
  max-width: 850px;
  margin: 20px auto;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.schedule-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 1.2rem;
  border-bottom: 1px solid #f0f0f0;
  padding: 1rem;
}

.subtitle {
  color: #888;
  font-size: 0.95rem;
  margin-top: 0.15rem;
}

.date-picker {
  width: 200px;
}

.list-view {
  margin-top: 1.2rem;
}

.appointment-timeline {
  padding: 1rem;
}

.appointment-footer {
  display: flex;
  justify-content: flex-end;
}

.no-appointments {
  text-align: center;
  padding: 1.2rem 0;
  color: #aaa;
}
</style>
