package com.trendster.campus.ui.fragment.user.faculty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentFacultyLoginBinding
import com.trendster.campus.ui.faculty.FacultyActivity

class FacultyLoginFragment : Fragment() {

    private var _binding: FragmentFacultyLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var etEmail: TextInputLayout
    private lateinit var etPassword: TextInputLayout
    private lateinit var btnSignup: TextView
    private lateinit var btnLogin: Button
    private lateinit var txtForgotPass: TextView
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFacultyLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        etEmail = binding.etFLoginEmail
        etPassword = binding.etFLoginPassword
        btnLogin = binding.btnfacultyLogin
        btnSignup = binding.btnGoToSignup
        txtForgotPass = binding.txtForgotPassword

        /** calling login function to login an existing facculty */
        login()

        /** If don't have an account, then take back to signup fragment */
        btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_facultyLoginFragment_to_facultySignupFragment)
        }

        /** If forgot password, then take to forgot password fragment */
        txtForgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_facultyLoginFragment_to_forgotFragment)
        }

        /** If back button is clicked , take back to previous fragment */
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    /** Function to login an existing faculty by matching its details from firebase */
    private fun login() {
        btnLogin.setOnClickListener {
            when {
                etEmail.editText?.text?.isEmpty() == true -> {
                    etEmail.error = "Fill this"
                    return@setOnClickListener
                }
                etPassword.editText?.text?.isEmpty() == true -> {
                    etPassword.error = "Fill this"
                    return@setOnClickListener
                }
                else -> {

                    /** Login faculty using Firebase */
                    auth.signInWithEmailAndPassword(etEmail.editText?.text.toString(), etPassword.editText?.text.toString())
                        .addOnCompleteListener(requireActivity()) { task ->

                            /** If login is successful, start the Faculty activity */
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("LoginSign", "signInWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user)
                            }

                            /** If log in fails, display an error message to the faculty */
                            else {
                                // If sign in fails, display a message to the user.
                                Log.w("LoginSign", "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    requireContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                updateUI(null)
                            }
                        }
                }
            }
        }
    }

    /** function to start Faculty activity if login is successful */
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(requireContext(), FacultyActivity::class.java))
            requireActivity().finish()
        }
    }
}
