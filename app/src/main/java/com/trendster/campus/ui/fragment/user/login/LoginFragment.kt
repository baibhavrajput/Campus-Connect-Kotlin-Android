package com.trendster.campus.ui.fragment.user.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cn.xm.weidongjian.progressbuttonlib.ProgressButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentLoginBinding
import com.trendster.campus.ui.MainActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var etEmail: TextInputLayout
    private lateinit var etPassword: TextInputLayout
    private lateinit var btnSignup: TextView
    private lateinit var btnLogin: ProgressButton
    private lateinit var txtForgotPass: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        etEmail = binding.etEmail
        etPassword = binding.etPasswordd
        btnLogin = binding.btnLogin
        btnSignup = binding.btnGoToSignup
        txtForgotPass = binding.txtForgotPassword

        /** calling login function to login an existing user */
        login()

        /** If don't have an account, then take back to signup fragment */
        btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        /** If forgot password, then take to forgot password fragment */
        txtForgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotFragment)
        }

        /** If faculty, then take to faculty login fragment */
        binding.btnfacultyLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_facultyLoginFragment)
        }

        /** If faculty, then take to faculty login fragment */
        binding.btnFacultyLoginLoginFrag.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_facultyLoginFragment)
        }

        return binding.root
    }

    /** Function to login an existing user by matching its details from firebase */
    private fun login() {
        btnLogin.setOnClickListener {
            when {

                etEmail.editText?.text?.isEmpty() == true -> {
                    binding.etEmail.error = "Fill this"
                    return@setOnClickListener
                }
                etPassword.editText?.text?.isEmpty() == true -> {
                    etPassword.error = "Fill this"
                    return@setOnClickListener
                }
                else -> {
                    btnLogin.startRotate()

                    /** Login user using Firebase */
                    auth.signInWithEmailAndPassword(etEmail.editText?.text.toString(), etPassword.editText?.text.toString())
                        .addOnCompleteListener(requireActivity()) { task ->

                            /** If login is successful,  */
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("LoginSign", "signInWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user)
                            }

                            /** If log in fails, display an error message to the user */
                            else {
                                // If sign in fails, display a message to the user.
                                Log.w("LoginSign", "signInWithEmail:failure", task.exception)
                                updateUI(null)
                            }
                        }
                }
            }
        }
    }

    /** function to start Main activity if login is successful else give error message */
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            btnLogin.animFinish()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
        else {
            Toast.makeText(
                requireContext(), "Authentication failed.",
                Toast.LENGTH_SHORT
            ).show()
//            btnLogin.stopProgressAnimation()
            btnLogin.animError()
            btnLogin.removeDrawable()
        }
    }
}
