package com.example.codingchallange.ui.main

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.codingchallange.R
import com.example.codingchallange.roomdb.entity.Question
import com.example.codingchallange.ui.viewmodel.QuizViewModel
import com.example.codingchallange.utils.QuizModel
import com.example.codingchallange.utils.Status
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class QuizQuestionsActivity : AppCompatActivity() {
    private var userName: String = "Khurram"

    private val questionsList = mutableListOf<QuizModel>()
    private var quizList = mutableListOf<Question>()

    private var currentQuestionIndex = 0;
    private var selectedAlternativeIndex = -1;
    private var isAnswerChecked = false;
    private var totalScore = 0;
    private val alternativesIds =
        arrayOf(R.id.optionOne, R.id.optionTwo, R.id.optionThree, R.id.optionFour,R.id.optionFour)

    private var tvQuestion: TextView? = null
    private var ivImage: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var tvProgress: TextView? = null
    private var btnSubmit: Button? = null
    private var tvAlternatives: ArrayList<TextView>? = null

    private val viewModel: QuizViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)


        tvQuestion = findViewById(R.id.tvQuestion)
        ivImage = findViewById(R.id.ivImage)
        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tvProgress)
        btnSubmit = findViewById(R.id.btnSubmit)
        tvAlternatives = arrayListOf(
            findViewById(R.id.optionOne),
            findViewById(R.id.optionTwo),
            findViewById(R.id.optionThree),
            findViewById(R.id.optionFour),
            findViewById(R.id.optionFive),
        )


        viewModel.quiz.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty())
                        Timber.d("questions length rec is ${it.data[0].questions.size}")
                    //adapter.setItems(ArrayList(it.data))

                    quizList.clear()
                    questionsList.clear()

                    it.data?.get(0)?.questions?.let { it1 -> quizList.addAll(it1) }


                    Timber.d("quiz list size is ${quizList.size}")

                    for (i in 0..quizList.lastIndex) {

                        val list = mutableListOf<String>()

                        var correctIndex = 1
                        when (quizList[i].correctAnswer) {
                            "A" -> {
                                correctIndex = 0
                            }

                            "B" -> {
                                correctIndex = 1
                            }

                            "C" -> {
                                correctIndex = 2
                            }

                            "D" -> {
                                correctIndex = 3
                            }

                            "E" -> {
                                correctIndex = 4
                            }

                            "A,B" -> {
                                correctIndex = 0
                            }

                            "A,C,D" -> {
                                correctIndex = 2
                            }

                            "A,B C D" -> {
                                correctIndex = 1
                            }
                        }

                        list.add(quizList[i].answers.A)
                        list.add(quizList[i].answers.B)
                        list.add(quizList[i].answers.C)
                        list.add(quizList[i].answers.D)
                        list.add(quizList[i].answers.E)

                        questionsList.add(
                            QuizModel(
                                questionText = quizList[i].question,
                                alternatives = list,
                                correctAnswerIndex = correctIndex,
                                type = quizList[i].type,
                                score = quizList[i].score

                            )
                        )

                    }

                    Timber.d("data list size is ${questionsList.size}")
                    Timber.d("data list size is ${Gson().toJson(questionsList)}")

                    updateQuestion()

                }

                Status.ERROR -> {

                    Timber.d("Error is ${it.message}")
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                }

                Status.LOADING -> {
                    Timber.d("data loading....")
                }
            }
        }




        btnSubmit?.setOnClickListener {
            if (!isAnswerChecked) {
                val anyAnswerIsChecked = selectedAlternativeIndex != -1
                if (!anyAnswerIsChecked) {
                    Toast.makeText(this, "Please, select an alternative", Toast.LENGTH_SHORT).show()
                } else {
                    val currentQuestion = questionsList[currentQuestionIndex]
                    if (
                        selectedAlternativeIndex == currentQuestion.correctAnswerIndex
                    ) {
                        answerView(
                            tvAlternatives!![selectedAlternativeIndex],
                            R.drawable.correct_option_border_bg
                        )
                        totalScore++
                    } else {
                        answerView(
                            tvAlternatives!![selectedAlternativeIndex],
                            R.drawable.wrong_option_border_bg
                        )
                        answerView(
                            tvAlternatives!![currentQuestion.correctAnswerIndex],
                            R.drawable.correct_option_border_bg
                        )
                    }

                    isAnswerChecked = true
                    btnSubmit?.text =
                        if (currentQuestionIndex == questionsList.size - 1) "FINISH" else "GO TO NEXT QUESTION"
                    selectedAlternativeIndex = -1
                }
            } else {
                if (currentQuestionIndex < questionsList.size - 1) {
                    currentQuestionIndex++
                    updateQuestion()
                } else {
                    /*val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra(Constants.USER_NAME, userName)
                    intent.putExtra(Constants.TOTAL_QUESTIONS, questionsList.size)
                    intent.putExtra(Constants.SCORE, totalScore)
                    startActivity(intent)
                    finish()*/
                }

                isAnswerChecked = false
            }
        }

        tvAlternatives?.let {
            for (optionIndex in it.indices) {
                it[optionIndex].let {
                    it.setOnClickListener {
                        if (!isAnswerChecked) {
                            selectedAlternativeView(it as TextView, optionIndex)
                        }
                    }
                }
            }
        }
    }

    private fun updateQuestion() {
        defaultAlternativesView()

        if (questionsList.isNotEmpty()) {
            // Render Question Text
            tvQuestion?.text = questionsList[currentQuestionIndex].questionText
            // Render Question Image
            //  ivImage?.setImageResource(questionsList[currentQuestionIndex].image)
            Glide.with(this)
                .load(questionsList[currentQuestionIndex].image)
                .into(ivImage!!)
            // progressBar
            progressBar?.progress = currentQuestionIndex + 1
            // Text of progress bar
            tvProgress?.text = "${currentQuestionIndex + 1}/${questionsList.size}"

            for (alternativeIndex in 0..questionsList[currentQuestionIndex].alternatives.lastIndex) {
                tvAlternatives?.get(alternativeIndex)?.text =
                    questionsList[currentQuestionIndex].alternatives[alternativeIndex]
            }

            btnSubmit?.text =
                if (currentQuestionIndex == questionsList.size - 1) "FINISH" else "SUBMIT"
        } else {
            Toast.makeText(this, "empty list", Toast.LENGTH_SHORT).show()
        }
    }

    private fun defaultAlternativesView() {
        for (alternativeTv in tvAlternatives!!) {
            alternativeTv.typeface = Typeface.DEFAULT
            alternativeTv.setTextColor(Color.parseColor("#7A8089"))
            alternativeTv.background = ContextCompat.getDrawable(
                this@QuizQuestionsActivity,
                R.drawable.default_option_border_bg
            )
        }
    }

    private fun selectedAlternativeView(option: TextView, index: Int) {
        defaultAlternativesView()
        selectedAlternativeIndex = index

        option.setTextColor(
            Color.parseColor("#363A43")
        )
        option.setTypeface(option.typeface, Typeface.BOLD)
        option.background = ContextCompat.getDrawable(
            this@QuizQuestionsActivity,
            R.drawable.selected_option_border_bg
        )
    }

    private fun answerView(view: TextView, drawableId: Int) {
        view.background = ContextCompat.getDrawable(
            this@QuizQuestionsActivity,
            drawableId
        )
        tvAlternatives!![selectedAlternativeIndex].setTextColor(
            Color.parseColor("#FFFFFF")
        )
    }

}