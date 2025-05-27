<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up - Modern Auth</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            overflow: hidden;
        }

        /* Animated background particles */
        .particles {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
            z-index: 1;
        }

        .particle {
            position: absolute;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 50%;
            animation: float 6s ease-in-out infinite;
        }

        .particle:nth-child(1) { width: 80px; height: 80px; left: 10%; animation-delay: 0s; }
        .particle:nth-child(2) { width: 120px; height: 120px; left: 20%; animation-delay: 2s; }
        .particle:nth-child(3) { width: 60px; height: 60px; left: 35%; animation-delay: 4s; }
        .particle:nth-child(4) { width: 100px; height: 100px; left: 70%; animation-delay: 1s; }
        .particle:nth-child(5) { width: 40px; height: 40px; left: 85%; animation-delay: 3s; }

        @keyframes float {
            0%, 100% { transform: translateY(0px) rotate(0deg); opacity: 0.7; }
            50% { transform: translateY(-100px) rotate(180deg); opacity: 0.3; }
        }

        .signup-container {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            box-shadow: 0 25px 50px rgba(0, 0, 0, 0.2);
            padding: 40px;
            width: 100%;
            max-width: 450px;
            position: relative;
            z-index: 10;
            transform: translateY(0);
            transition: all 0.3s ease;
            margin: 20px;
        }

        .signup-container:hover {
            transform: translateY(-5px);
            box-shadow: 0 35px 60px rgba(0, 0, 0, 0.3);
        }

        .signup-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .logo {
            width: 80px;
            height: 80px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            border-radius: 50%;
            margin: 0 auto 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2rem;
            color: white;
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.05); }
            100% { transform: scale(1); }
        }

        .signup-title {
            font-size: 2rem;
            font-weight: 700;
            color: #333;
            margin-bottom: 10px;
        }

        .signup-subtitle {
            color: #666;
            font-size: 0.95rem;
        }

        .form-group {
            margin-bottom: 20px;
            position: relative;
        }

        .form-group i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #667eea;
            font-size: 1.1rem;
            z-index: 2;
        }

        .form-input {
            width: 100%;
            padding: 15px 15px 15px 45px;
            border: 2px solid #e1e8ed;
            border-radius: 12px;
            font-size: 1rem;
            transition: all 0.3s ease;
            background: #f8f9ff;
        }

        .form-input:focus {
            outline: none;
            border-color: #667eea;
            background: white;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .form-input::placeholder {
            color: #aaa;
        }

        .form-row {
            display: flex;
            gap: 15px;
        }

        .form-row .form-group {
            flex: 1;
        }

        .password-strength {
            margin-top: 8px;
            font-size: 0.8rem;
        }

        .strength-bar {
            height: 4px;
            background: #e1e8ed;
            border-radius: 2px;
            margin-top: 5px;
            overflow: hidden;
        }

        .strength-fill {
            height: 100%;
            width: 0%;
            border-radius: 2px;
            transition: all 0.3s ease;
        }

        .strength-weak { background: #ff4757; width: 25%; }
        .strength-fair { background: #ffa502; width: 50%; }
        .strength-good { background: #26de81; width: 75%; }
        .strength-strong { background: #2ed573; width: 100%; }

        .checkbox-group {
            display: flex;
            align-items: flex-start;
            gap: 12px;
            margin-bottom: 25px;
            font-size: 0.9rem;
            color: #666;
        }

        .checkbox-group input[type="checkbox"] {
            margin-top: 2px;
            transform: scale(1.2);
            accent-color: #667eea;
        }

        .checkbox-group a {
            color: #667eea;
            text-decoration: none;
            transition: color 0.3s ease;
        }

        .checkbox-group a:hover {
            color: #764ba2;
        }

        .signup-btn {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            border: none;
            border-radius: 12px;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }

        .signup-btn::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
            transition: left 0.5s ease;
        }

        .signup-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .signup-btn:hover::before {
            left: 100%;
        }

        .signup-btn:active {
            transform: translateY(0);
        }

        .signup-btn:disabled {
            opacity: 0.7;
            cursor: not-allowed;
        }

        .divider {
            text-align: center;
            margin: 30px 0;
            position: relative;
            color: #666;
        }

        .divider::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 0;
            right: 0;
            height: 1px;
            background: #e1e8ed;
        }

        .divider span {
            background: rgba(255, 255, 255, 0.95);
            padding: 0 20px;
            position: relative;
        }

        .social-login {
            display: flex;
            gap: 15px;
            margin-bottom: 25px;
        }

        .social-btn {
            flex: 1;
            padding: 12px;
            border: 2px solid #e1e8ed;
            border-radius: 12px;
            background: white;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.2rem;
        }

        .social-btn.google {
            color: #db4437;
        }

        .social-btn.facebook {
            color: #4267B2;
        }

        .social-btn.twitter {
            color: #1da1f2;
        }

        .social-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        .login-link {
            text-align: center;
            color: #666;
            font-size: 0.95rem;
        }

        .login-link a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.3s ease;
        }

        .login-link a:hover {
            color: #764ba2;
        }

        .error-message {
            background: #fee;
            border: 1px solid #fcc;
            color: #c33;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 0.9rem;
            display: none;
        }

        .success-message {
            background: #efe;
            border: 1px solid #cfc;
            color: #3c3;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 0.9rem;
            display: none;
        }

        .field-error {
            border-color: #ff4757 !important;
            background: #fff5f5 !important;
        }

        .validation-message {
            font-size: 0.8rem;
            color: #ff4757;
            margin-top: 5px;
            display: none;
        }

        @media (max-width: 480px) {
            .signup-container {
                margin: 15px;
                padding: 30px 25px;
            }
            
            .signup-title {
                font-size: 1.7rem;
            }

            .form-row {
                flex-direction: column;
                gap: 0;
            }
        }
    </style>
</head>
<body>
    <!-- Animated background particles -->
    <div class="particles">
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
    </div>

    <div class="signup-container">
        <div class="signup-header">
            <div class="logo">
                <i class="fas fa-user-plus"></i>
            </div>
            <h1 class="signup-title">Create Account</h1>
            <p class="signup-subtitle">Join us today and get started</p>
        </div>

        <!-- Error/Success Messages -->
        <div class="error-message" id="errorMessage">
            <i class="fas fa-exclamation-circle"></i>
            <span id="errorText"></span>
        </div>
        
        <div class="success-message" id="successMessage">
            <i class="fas fa-check-circle"></i>
            <span id="successText"></span>
        </div>

        <form action="SignUp" method="post" id="signupForm">
            <div class="form-row">
                <div class="form-group">
                    <i class="fas fa-user"></i>
                    <input type="text" name="firstName" class="form-input" placeholder="First Name" required>
                    <div class="validation-message" id="firstNameError"></div>
                </div>
                <div class="form-group">
                    <i class="fas fa-user"></i>
                    <input type="text" name="lastName" class="form-input" placeholder="Last Name" required>
                    <div class="validation-message" id="lastNameError"></div>
                </div>
            </div>

            <div class="form-group">
                <i class="fas fa-envelope"></i>
                <input type="email" name="email" class="form-input" placeholder="Email Address" required>
                <div class="validation-message" id="emailError"></div>
            </div>

            <div class="form-group">
                <i class="fas fa-phone"></i>
                <input type="tel" name="phone" class="form-input" placeholder="Phone Number" required>
                <div class="validation-message" id="phoneError"></div>
            </div>

            <div class="form-group">
                <i class="fas fa-lock"></i>
                <input type="password" name="password" class="form-input" placeholder="Password" required id="password">
                <div class="password-strength" id="passwordStrength" style="display: none;">
                    <div class="strength-bar">
                        <div class="strength-fill" id="strengthFill"></div>
                    </div>
                    <div id="strengthText" style="margin-top: 5px;"></div>
                </div>
                <div class="validation-message" id="passwordError"></div>
            </div>

            <div class="form-group">
                <i class="fas fa-lock"></i>
                <input type="password" name="confirmPassword" class="form-input" placeholder="Confirm Password" required id="confirmPassword">
                <div class="validation-message" id="confirmPasswordError"></div>
            </div>

            <div class="checkbox-group">
                <input type="checkbox" name="agreeTerms" required id="agreeTerms">
                <label for="agreeTerms">
                    I agree to the <a href="#" onclick="return false;">Terms of Service</a> and 
                    <a href="#" onclick="return false;">Privacy Policy</a>
                </label>
            </div>

            <div class="checkbox-group" style="margin-top: -15px;">
                <input type="checkbox" name="newsletter" id="newsletter">
                <label for="newsletter">
                    Subscribe to our newsletter for updates and promotions
                </label>
            </div>

            <button type="submit" class="signup-btn">
                <i class="fas fa-user-plus" style="margin-right: 8px;"></i>
                Create Account
            </button>
        </form>

        <div class="divider">
            <span>or sign up with</span>
        </div>

        <div class="social-login">
            <button class="social-btn google" onclick="socialSignup('google')">
                <i class="fab fa-google"></i>
            </button>
            <button class="social-btn facebook" onclick="socialSignup('facebook')">
                <i class="fab fa-facebook-f"></i>
            </button>
            <button class="social-btn twitter" onclick="socialSignup('twitter')">
                <i class="fab fa-twitter"></i>
            </button>
        </div>

        <div class="login-link">
            Already have an account? <a href="#" onclick="return false;">Sign in here</a>
        </div>
    </div>

    <script>
        // Form validation and submission
        document.getElementById('signupForm').addEventListener('submit', function(e) {
           
            
            if (!validateForm()) {
            	e.preventDefault();
                return;
            }
            
            // Show loading state
            const submitBtn = this.querySelector('button[type="submit"]');
            const originalText = submitBtn.innerHTML;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin" style="margin-right: 8px;"></i>Creating Account...';
            submitBtn.disabled = true;
            
            // For demo purposes - simulate signup process
            setTimeout(() => {
                // Reset button
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
                
                showSuccess('Account created successfully! Please check your email for verification.');
                
                // Clear form after successful submission
                setTimeout(() => {
                    this.reset();
                    document.getElementById('passwordStrength').style.display = 'none';
                }, 2000);
            }, 2500);
        });
        
        function validateForm() {
            let isValid = true;
            const form = document.getElementById('signupForm');
            
            // Clear previous errors
            clearAllErrors();
            
            // Validate first name
            const firstName = form.firstName.value.trim();
            if (!firstName || firstName.length < 2) {
                showFieldError('firstName', 'First name must be at least 2 characters');
                isValid = false;
            }
            
            // Validate last name
            const lastName = form.lastName.value.trim();
            if (!lastName || lastName.length < 2) {
                showFieldError('lastName', 'Last name must be at least 2 characters');
                isValid = false;
            }
            
            // Validate email
            const email = form.email.value.trim();
            if (!email || !isValidEmail(email)) {
                showFieldError('email', 'Please enter a valid email address');
                isValid = false;
            }
            
            // Validate phone
            const phone = form.phone.value.trim();
            if (!phone || !isValidPhone(phone)) {
                showFieldError('phone', 'Please enter a valid phone number');
                isValid = false;
            }
            
            // Validate password
            const password = form.password.value;
            const passwordStrength = checkPasswordStrength(password);
            if (!password || passwordStrength.score < 2) {
                showFieldError('password', 'Password must be stronger (at least 8 characters with mixed case, numbers, and symbols)');
                isValid = false;
            }
            
            // Validate confirm password
            const confirmPassword = form.confirmPassword.value;
            if (password !== confirmPassword) {
                showFieldError('confirmPassword', 'Passwords do not match');
                isValid = false;
            }
            
            // Validate terms agreement
            if (!form.agreeTerms.checked) {
                showError('You must agree to the Terms of Service and Privacy Policy');
                isValid = false;
            }
            
            return isValid;
        }
        
        function clearAllErrors() {
            // Clear field errors
            document.querySelectorAll('.field-error').forEach(field => {
                field.classList.remove('field-error');
            });
            
            // Hide all validation messages
            document.querySelectorAll('.validation-message').forEach(msg => {
                msg.style.display = 'none';
                msg.textContent = '';
            });
            
            // Hide general error message
            document.getElementById('errorMessage').style.display = 'none';
        }
        
        function showFieldError(fieldName, message) {
            // Get the input field
            const field = document.querySelector(`[name="${fieldName}"]`);
            if (!field) {
                console.error(`Field with name "${fieldName}" not found`);
                return;
            }
            
            // Get the error message element
            const errorElement = document.getElementById(`${fieldName}Error`);
            if (!errorElement) {
                console.error(`Error element with id "${fieldName}Error" not found`);
                return;
            }
            
            // Apply error styling and show message
            field.classList.add('field-error');
            errorElement.textContent = message;
            errorElement.style.display = 'block';
            
            // Focus on the first error field
            if (!document.querySelector('.field-error:focus')) {
                field.focus();
            }
        }
        
        function isValidEmail(email) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return emailRegex.test(email);
        }
        
        function isValidPhone(phone) {
            // Remove all non-digit characters for validation
            const cleanPhone = phone.replace(/\D/g, '');
            return cleanPhone.length >= 10;
        }
        
        function checkPasswordStrength(password) {
            let score = 0;
            let feedback = [];
            
            if (password.length >= 8) score++;
            else feedback.push('at least 8 characters');
            
            if (/[a-z]/.test(password)) score++;
            else feedback.push('lowercase letters');
            
            if (/[A-Z]/.test(password)) score++;
            else feedback.push('uppercase letters');
            
            if (/[0-9]/.test(password)) score++;
            else feedback.push('numbers');
            
            if (/[^A-Za-z0-9]/.test(password)) score++;
            else feedback.push('special characters');
            
            const levels = ['Very Weak', 'Weak', 'Fair', 'Good', 'Strong'];
            const classes = ['', 'strength-weak', 'strength-fair', 'strength-good', 'strength-strong'];
            
            return {
                score: score,
                level: levels[score] || 'Very Weak',
                className: classes[score] || '',
                feedback: feedback
            };
        }
        
        // Password strength indicator
        document.getElementById('password').addEventListener('input', function() {
            const password = this.value;
            const strengthDiv = document.getElementById('passwordStrength');
            const strengthFill = document.getElementById('strengthFill');
            const strengthText = document.getElementById('strengthText');
            
            if (password.length > 0) {
                strengthDiv.style.display = 'block';
                const strength = checkPasswordStrength(password);
                
                // Clear previous classes
                strengthFill.className = 'strength-fill';
                if (strength.className) {
                    strengthFill.classList.add(strength.className);
                }
                
                strengthText.textContent = `${strength.level}${strength.feedback.length > 0 ? ' - Add: ' + strength.feedback.join(', ') : ''}`;
            } else {
                strengthDiv.style.display = 'none';
            }
        });
        
        // Real-time confirm password validation
        document.getElementById('confirmPassword').addEventListener('input', function() {
            const password = document.getElementById('password').value;
            const confirmPassword = this.value;
            const errorDiv = document.getElementById('confirmPasswordError');
            
            if (confirmPassword.length > 0 && password !== confirmPassword) {
                this.classList.add('field-error');
                errorDiv.textContent = 'Passwords do not match';
                errorDiv.style.display = 'block';
            } else {
                this.classList.remove('field-error');
                errorDiv.style.display = 'none';
            }
        });
        
        // Clear field errors on input
        document.querySelectorAll('.form-input').forEach(input => {
            input.addEventListener('input', function() {
                // Clear field error styling when user starts typing
                if (this.classList.contains('field-error')) {
                    this.classList.remove('field-error');
                    const fieldName = this.getAttribute('name');
                    if (fieldName) {
                        const errorElement = document.getElementById(`${fieldName}Error`);
                        if (errorElement) {
                            errorElement.style.display = 'none';
                        }
                    }
                }
            });
        });
        
        function showError(message) {
            const errorDiv = document.getElementById('errorMessage');
            const errorText = document.getElementById('errorText');
            const successDiv = document.getElementById('successMessage');
            
            successDiv.style.display = 'none';
            errorText.textContent = message;
            errorDiv.style.display = 'block';
            
            // Auto hide after 5 seconds
            setTimeout(() => {
                errorDiv.style.display = 'none';
            }, 5000);
        }
        
        function showSuccess(message) {
            const successDiv = document.getElementById('successMessage');
            const successText = document.getElementById('successText');
            const errorDiv = document.getElementById('errorMessage');
            
            errorDiv.style.display = 'none';
            successText.textContent = message;
            successDiv.style.display = 'block';
            
            // Auto hide after 5 seconds
            setTimeout(() => {
                successDiv.style.display = 'none';
            }, 5000);
        }
        
        function socialSignup(provider) {
            showSuccess(`Redirecting to ${provider.charAt(0).toUpperCase() + provider.slice(1)} signup...`);
            // In a real application, you would redirect to the social auth provider
            setTimeout(() => {
                console.log(`Would redirect to ${provider} signup`);
            }, 1500);
        }
        
        // Add smooth focus effects
        document.querySelectorAll('.form-input').forEach(input => {
            input.addEventListener('focus', function() {
                const icon = this.parentElement.querySelector('i');
                if (icon) {
                    icon.style.color = '#667eea';
                }
            });
            
            input.addEventListener('blur', function() {
                const icon = this.parentElement.querySelector('i');
                if (icon) {
                    icon.style.color = '#aaa';
                }
            });
        });
        
        // Demo functionality for testing errors
        document.addEventListener('DOMContentLoaded', function() {
            // You can test the form by trying to submit with invalid data
            console.log('Form validation demo loaded. Try submitting with invalid data to see error handling.');
        });
    </script>
</body>
</html>