export interface Appointment {
  id: number;
  patient: Patient;
  appointmentType: AppointmentType;
  appointmentTime: Date;
  appointmentEndTime: Date;
  appointmentStatus: string;
  description: string;
}

export interface Unavailbility {
  id: number;
  startTime: Date;
  endTime: Date;
  description: string
}

interface Patient {
  name: string
}

interface AppointmentType {
  name: string
  durationMinutes: number
}