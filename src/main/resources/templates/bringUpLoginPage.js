const backendURL = "http://localhost:8080/api/user";

document.addEventListener("DOMContentLoaded", function () {
  const dotContainer = document.getElementById("swinging-dots");
  const dotCount = 40;
  for (let i = 0; i < dotCount; i++) {
    let dot = document.createElement("div");
    dot.classList.add("dot");
    let randomTop = Math.random() * 100;
    let randomLeft = Math.random() * 100;
    let animationDuration = Math.random() * 10 + 10;
    dot.style.top = `${randomTop}%`;
    dot.style.left = `${randomLeft}%`;
    dot.style.animationDuration = `${animationDuration}s`;
    dotContainer.appendChild(dot);
  }
});


// Sign In
async function signIn() {
  const email = document.getElementById("email").value;
  if (!email) {
    alert("Please enter your email to sign in.");
    return;
  }

  try {
    const response = await fetch(`${backendURL}/getUser?emailID=${encodeURIComponent(email)}`, {
      method: "GET",
    });

    const result = await response.json(); // expecting JSON { isVerified: 1 or 0 }

//    console.log("SignIn Response:", result);

    if (result.isverified === 1) {
      document.getElementById("responseMessage").textContent = "Sign In Success, Redirecting to Main Page..";
      document.getElementById("responseMessage").style.color = "green";
      setTimeout(() => {
        window.location.href = "bringUpDashBoard.html";
      }, 2000);
    } else {
      document.getElementById("responseMessage").textContent = "Account not verified. Please register using OTP.";
      document.getElementById("responseMessage").style.color = "red";
    }

  } catch (error) {
    console.error("Error during sign in:", error);
    const msg = document.getElementById("responseMessage");
    msg.textContent = "Account not verified. Please register using OTP.";
    msg.style.color = "red";
  }
}

// Send OTP
let isCooldown = false;
let cooldownTimer;

async function sendOTP() {
  const email = document.getElementById("email").value;
  const msg = document.getElementById("responseMessage");
  const registerBtn = document.getElementById("registerBtn"); // Make sure button has this ID

  if (!email) {
    alert("Please enter a valid email!");
    return;
  }

  if (isCooldown) {
      msg.textContent = "Please wait before resending OTP.";
      msg.style.color = "orange";
      return;
    }

  try {
    const checkResponse = await fetch(`${backendURL}/getUser?emailID=${encodeURIComponent(email)}`);
//    console.log("User check status:", checkResponse.status); // ✅ For debugging

    if (checkResponse.ok) {
      const userData = await checkResponse.json();
      msg.textContent = "This email is already registered. Please Sign In.";
      msg.style.color = "green";
      return;
    }

    // 🔄 Immediately change button text to give feedback
    registerBtn.textContent = "OTP Sent...";

    // User not found (new user)
    if (checkResponse.status === 400) {
      const response = await fetch(`${backendURL}/sendOtp?email=${encodeURIComponent(email)}`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
      });

      const data = await response.text();
      msg.textContent = data;
      msg.style.color = data.toLowerCase().includes("otp") ? "green" : "red";

      if (data.toLowerCase().includes("otp")) {
        document.getElementById("otpSection").classList.remove("hidden");

    // ✅ Start 60s cooldown countdown
        isCooldown = true;
        let timeLeft = 60;

        registerBtn.disabled = true;
        singInBtn.disabled = true;
        registerBtn.textContent = `Resend OTP in ${timeLeft}s`;

        countdownInterval = setInterval(() => {
            timeLeft--;
            registerBtn.textContent = `Resend OTP in ${timeLeft}s`;

            if (timeLeft <= 0) {
                clearInterval(countdownInterval);
                registerBtn.disabled = false;
                singInBtn.disabled = false;
                registerBtn.textContent = "Register";
                isCooldown = false;
              }
            }, 1000);
          }
        }
      else {
      msg.textContent = "Unexpected error during registration check.";
      msg.style.color = "red";
      registerBtn.textContent = "Register"; // Reset
    }

  } catch (error) {
    console.error("Error:", error);
    msg.textContent = "Failed to check registration or send OTP.";
    msg.style.color = "red";
    registerBtn.textContent = "Register"; // Reset
  }
}


// Verify OTP
async function verifyOTP() {
  const email = document.getElementById("email").value;
  const otp = document.getElementById("otp").value;

  if (!otp) {
    alert("Please enter OTP!");
    return;
  }

  try {
    const response = await fetch(`${backendURL}/verifyOtp?email=${encodeURIComponent(email)}&otp=${otp}`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
    });

    const data = await response.text();
    const responseMsg = document.getElementById("responseMessage");
    responseMsg.textContent = data;
    responseMsg.style.color = data.toLowerCase().includes("success") ? "green" : "red";

    if (data.toLowerCase().includes("success")) {
      setTimeout(() => {
        window.location.href = "bringUpDashBoard.html";
      }, 2000);
    }

  } catch (error) {
    console.error("Error:", error);
    document.getElementById("responseMessage").textContent = "Failed to verify OTP.";
  }
}
