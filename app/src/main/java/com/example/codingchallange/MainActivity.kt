package com.example.codingchallange

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.codingchallange.databinding.ActivityMainBinding
import com.example.codingchallange.databinding.ItemOptionBinding
import com.example.codingchallange.roomdb.entity.Question
import com.example.codingchallange.ui.viewmodel.QuestionViewModel
import com.example.codingchallange.ui.viewmodel.QuizViewModel
import com.example.codingchallange.utils.ExtensionFunctions.hide
import com.example.codingchallange.utils.QuizModel
import com.example.codingchallange.utils.Status
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Random

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val viewModel: QuizViewModel by viewModels()
    private val questionModel: QuestionViewModel by viewModels()
    private var optionSelected = mutableListOf<String>()

    private var quizList = mutableListOf<Question>()

    private var questionTotal: Int = 0

    private var score = 0

    private var selectedOptions = mutableListOf<Int>()

    private var dataList = mutableListOf<QuizModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        questionModel.questionNumber.postValue(0)

        initToolbar()

        setupObservers()

    }


    private fun initToolbar() {
        val title = getString(R.string.app_name)
        mainBinding.toolbar.title = title
        setSupportActionBar(mainBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }

    private fun setupObservers() {

        viewModel.quiz.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty())
                        Timber.d("questions length rec is ${it.data[0].questions.size}")


                    quizList.clear()
                    dataList.clear()

                    it.data?.get(0)?.questions?.let { it1 -> quizList.addAll(it1) }


                    for (i in 0..quizList.lastIndex) {

                        val list = mutableListOf<String>()

                        var correctIndex = 1
                        when (quizList[i].correctAnswer) {
                            "A" -> {
                                correctIndex = 1
                            }

                            "B" -> {
                                correctIndex = 2
                            }

                            "C" -> {
                                correctIndex = 3
                            }

                            "D" -> {
                                correctIndex = 4
                            }

                            "E" -> {
                                correctIndex = 5
                            }
                        }

                        list.add(quizList[i].answers.A)
                        list.add(quizList[i].answers.B)
                        list.add(quizList[i].answers.C)
                        list.add(quizList[i].answers.D)
                        list.add(quizList[i].answers.E)

                        dataList.add(
                            QuizModel(
                                questionText = quizList[i].question,
                                alternatives = list,
                                correctAnswerIndex = correctIndex,
                                type = quizList[i].type,
                                score = quizList[i].score

                            )
                        )

                    }

                    Timber.d("data list size is ${Gson().toJson(dataList)}")


                    questionTotal = dataList.size

                    mainBinding.lpiProgress.progress = 1
                    mainBinding.lpiProgress.max = questionTotal

                    mainBinding.tvQuestionNumber.text =
                        "Questions ${mainBinding.lpiProgress.progress} of $questionTotal"

                    Timber.d("random question is ${quizList[Random().nextInt(dataList.size)]}")


                    questionModel.questionNumber.observe(this) { qNum ->

                        val question = quizList[qNum]
                        mainBinding.lpiProgress.progress = qNum + 1
                        updateQuestion(question)


                        mainBinding.tvQuestionNumber.text = getString(
                            R.string.question_progress,
                            if ((qNum + 1).toString().length > 1) (qNum + 1).toString() else "0${(qNum + 1)}",
                            if (questionTotal.toString().length > 1) questionTotal.toString() else "0$questionTotal"
                        )

                        mainBinding.btnNext.text =
                            if (qNum == questionTotal - 1) "Submit" else getString(R.string.next)


                        mainBinding.btnNext.setOnClickListener {
                            if (qNum == questionTotal - 1) {
                                // timer.cancel()
                                evaluateQuizResult(question)
                                showResult(totalQuestions = questionTotal)
                            } else {
                                if (selectedOptions.size > 0) {
                                    evaluateQuizResult(question)
                                    resetOptions()
                                }
                                questionModel.questionNumber.postValue(qNum + 1)
                            }
                        }
                    }
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

    }

    private fun resetOptions() {
        selectedOptions.clear()
        mainBinding.option1.llOption.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.shimmerDark)
        mainBinding.option2.llOption.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.shimmerDark)
        mainBinding.option3.llOption.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.shimmerDark)
        mainBinding.option4.llOption.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.shimmerDark)
    }

    @SuppressLint("StringFormatInvalid")
    private fun showResult(totalQuestions: Int) {
        mainBinding.clContent.visibility = View.GONE
        mainBinding.clControls.visibility = View.GONE
        mainBinding.quizResult.apply {
            clResults.visibility = View.VISIBLE
            if (score == totalQuestions) {
                tvResult.text = getString(R.string.congratulations)
            } else {
                tvResult.text = getString(R.string.better_luck_next_time)
            }

            tvScore.text = "$score"

            btnShareResults.setOnClickListener {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.share_results, score, totalQuestions)
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }

        }
    }

    private fun evaluateQuizResult(question: Question) {

        optionSelected.forEach {





        }
      /*  if (optionSelected == question.correctAnswer) {





            Timber.d("condition is True and correct ans is ${question.correctAnswer}")
            score += question.score
        }*/
    }


    private fun selectOption(view: ItemOptionBinding, option: Int) {
        if (selectedOptions.contains(option)) {
            selectedOptions.remove(option)
            view.llOption.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.shimmerDark)
        } else {
            selectedOptions.add(option)
            view.llOption.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.success)
        }
    }


    private fun updateQuestion(question: Question) {
        mainBinding.tvQuestion.text = question.question
        mainBinding.option1.tvOption.text = question.answers.A
        mainBinding.option2.tvOption.text = question.answers.B
        mainBinding.option3.tvOption.text = question.answers.C
        mainBinding.option4.tvOption.text = question.answers.D

        mainBinding.option1.llOption.setOnClickListener {
            optionSelected.add( "A")
            selectOption(mainBinding.option1, 1)
        }
        mainBinding.option2.llOption.setOnClickListener {
            optionSelected.add( "B")
            selectOption(mainBinding.option2, 2)
        }
        mainBinding.option3.llOption.setOnClickListener {
            optionSelected.add( "C")
            selectOption(mainBinding.option3, 3)
        }
        mainBinding.option4.llOption.setOnClickListener {
            optionSelected.add( "D")
            selectOption(mainBinding.option4, 4)
        }
    }

    private fun setNumberOfOptions(optionCount: Int) {
        when (optionCount) {
            2 -> {
                mainBinding.option3.llOption.hide()
                mainBinding.option4.llOption.hide()
                mainBinding.option5.llOption.hide()
            }

            3 -> {
                mainBinding.option4.llOption.hide()
                mainBinding.option5.llOption.hide()
            }

            4 -> {
                mainBinding.option5.llOption.hide()
            }
        }
    }


}