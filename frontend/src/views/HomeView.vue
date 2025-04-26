<template>
  <div class="home">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero-content">
        <h1>Welcome to Smiling Dental Clinic</h1>
        <p class="subtitle">Your trusted partner in dental care and beautiful smiles</p>
        <router-link to="/appointment" class="cta-button">Book an Appointment</router-link>
      </div>
    </section>

    <!-- Services Section -->
    <section class="services">
      <h2>Our Services</h2>
      <div class="services-grid">
        <div class="service-card">
          <div class="service-icon">🦷</div>
          <h3>General Dentistry</h3>
          <p>Regular check-ups, cleanings, and preventive care to maintain your oral health.</p>
        </div>
        <div class="service-card">
          <div class="service-icon">😁</div>
          <h3>Cosmetic Dentistry</h3>
          <p>Teeth whitening, veneers, and smile makeovers to enhance your natural beauty.</p>
        </div>
        <div class="service-card">
          <div class="service-icon">🦷</div>
          <h3>Orthodontics</h3>
          <p>Modern braces and aligners for perfectly aligned teeth and improved bite.</p>
        </div>
        <div class="service-card">
          <div class="service-icon">🦷</div>
          <h3>Emergency Care</h3>
          <p>Immediate attention for dental emergencies and urgent care needs.</p>
        </div>
      </div>
    </section>

    <!-- About Section -->
    <section class="about">
      <div class="about-content">
        <div class="about-text">
          <h2>About Smiling Dental Clinic</h2>
          <p>
            At Smiling Dental Clinic, we believe that everyone deserves a healthy, beautiful smile.
            Our team of experienced dental professionals is committed to providing personalized,
            gentle care in a comfortable environment.
          </p>
          <p>
            We use state-of-the-art technology and the latest dental techniques to ensure the best
            possible outcomes for our patients. Your comfort and satisfaction are our top
            priorities.
          </p>
        </div>
        <div class="about-image">
          <div class="image-placeholder">Modern Dental Office</div>
        </div>
      </div>
    </section>

    <!-- Testimonials Section -->
    <section class="testimonials">
      <h2>What Our Patients Say</h2>
      <div class="testimonials-grid">
        <div class="testimonial-card">
          <p class="quote">
            "The team at Smiling Dental Clinic made me feel comfortable from the moment I walked in.
            My smile has never looked better!"
          </p>
          <p class="author">- Sarah Johnson</p>
        </div>
        <div class="testimonial-card">
          <p class="quote">
            "Professional, friendly, and thorough. I highly recommend their services to anyone
            looking for quality dental care."
          </p>
          <p class="author">- Michael Chen</p>
        </div>
        <div class="testimonial-card">
          <p class="quote">
            "The modern facilities and caring staff made my dental visit a pleasant experience.
            Great results!"
          </p>
          <p class="author">- Emily Rodriguez</p>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta">
      <div class="cta-content">
        <h2>Ready to Transform Your Smile?</h2>
        <p>
          Schedule your appointment today and take the first step towards a healthier, more
          beautiful smile.
        </p>
        <router-link to="/appointment" class="cta-button">Book Now</router-link>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
// Component logic can be added here if needed
</script>

<style scoped>
.home {
  min-height: 100vh;
}

/* Hero Section */
.hero {
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('../assets/landing.jpg');
  background-size: cover;
  background-position: center;
  height: 50vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: white;
  padding: 0 1rem;
}

.hero-content {
  max-width: 800px;
}

.hero h1 {
  font-size: 3.5rem;
  margin-bottom: 1rem;
  font-weight: 700;
}

.subtitle {
  font-size: 1.5rem;
  margin-bottom: 2rem;
  opacity: 0.9;
}

/* Services Section */
.services {
  padding: 5rem 2rem;
  background-color: #f8f9fa;
}

.services h2 {
  text-align: center;
  margin-bottom: 3rem;
  font-size: 2.5rem;
  color: #2c3e50;
}

.services-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.service-card {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
  transition: transform 0.3s ease;
}

.service-card:hover {
  transform: translateY(-5px);
}

.service-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.service-card h3 {
  color: #2c3e50;
  margin-bottom: 1rem;
}

.about {
  padding: 5rem 2rem;
  background-color: white;
}

.about-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  align-items: center;
}

.about-text h2 {
  color: #2c3e50;
  margin-bottom: 1.5rem;
  font-size: 2.5rem;
}

.about-text p {
  margin-bottom: 1rem;
  line-height: 1.6;
  color: #666;
}

.about-image {
  background: #f0f0f0;
  height: 400px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}

/* Testimonials Section */
.testimonials {
  padding: 5rem 2rem;
  background-color: #f8f9fa;
}

.testimonials h2 {
  text-align: center;
  margin-bottom: 3rem;
  font-size: 2.5rem;
  color: #2c3e50;
}

.testimonials-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.testimonial-card {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.quote {
  font-style: italic;
  margin-bottom: 1rem;
  line-height: 1.6;
}

.author {
  font-weight: 600;
  color: #2c3e50;
}

/* CTA Section */
.cta {
  padding: 5rem 2rem;
  background: linear-gradient(135deg, #4a90e2, #357abd);
  color: white;
  text-align: center;
}

.cta-content {
  max-width: 800px;
  margin: 0 auto;
}

.cta h2 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.cta p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  opacity: 0.9;
}

/* Common Styles */
.cta-button {
  display: inline-block;
  padding: 1rem 2rem;
  background-color: #4a90e2;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  font-weight: 600;
  transition: background-color 0.3s ease;
}

.cta-button:hover {
  background-color: #357abd;
}

@media (max-width: 768px) {
  .hero h1 {
    font-size: 2.5rem;
  }

  .subtitle {
    font-size: 1.2rem;
  }

  .about-content {
    grid-template-columns: 1fr;
  }

  .about-image {
    height: 300px;
  }
}
</style>
