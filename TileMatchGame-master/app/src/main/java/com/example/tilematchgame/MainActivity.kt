package com.example.tilematchgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.tilematchgame.uilte.OnSwipeListener
import java.util.Arrays.asList

class MainActivity : AppCompatActivity() {


    var tiles = intArrayOf(
        R.drawable.leaf,
        R.drawable.fire,
        R.drawable.dragon,
        R.drawable.bug,
        R.drawable.water,
        R.drawable.thunder
    )
    var widthOfTile :Int = 0
    var noOfTile :Int = 8
    var widthOfScreen :Int = 0
    lateinit var tile :ArrayList<ImageView>
    var tileToBeDragged :Int = 0
    var tileToBeReplaced :Int = 0
    var notTile :Int = R.drawable.transparent

    lateinit var mHandler: Handler
    private lateinit var scoreResult :TextView
    var score = 0
    var interval = 100L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreResult = findViewById(R.id.score)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        widthOfScreen = displayMetrics.widthPixels

        var heightOfScreens = displayMetrics.heightPixels

        widthOfTile =widthOfScreen / noOfTile

        tile = ArrayList()
        createBoard()

        for (imageView in tile){
            imageView.setOnTouchListener(object :OnSwipeListener(this){
                override fun onSwipeRight() {
                    super.onSwipeRight()
                    tileToBeDragged =imageView.id
                    tileToBeReplaced = tileToBeDragged + 1
                    tileInterChange()
                }

                override fun onSwipeLeft() {
                    super.onSwipeLeft()
                    tileToBeDragged =imageView.id
                    tileToBeReplaced = tileToBeDragged - 1
                    tileInterChange()
                }

                override fun onSwipeTop() {
                    super.onSwipeTop()
                    tileToBeDragged =imageView.id
                    tileToBeReplaced = tileToBeDragged - noOfTile
                    tileInterChange()
                }

                override fun onSwipeBottom() {
                    super.onSwipeBottom()
                    tileToBeDragged =imageView.id
                    tileToBeReplaced = tileToBeDragged + noOfTile
                    tileInterChange()
                }
            })
        }

        mHandler = Handler()
        startRepeat()

    }


    private fun tileInterChange() {
        var background :Int = tile.get(tileToBeReplaced).tag as Int
        var background1 :Int = tile.get(tileToBeDragged).tag as Int

        tile.get(tileToBeDragged).setImageResource(background)
        tile.get(tileToBeReplaced).setImageResource(background1)

        tile.get(tileToBeDragged).setTag(background)
        tile.get(tileToBeReplaced).setTag(background1)
    }

    private fun checkRowForThree(){
        for(i in 0..61){
            var chosenTile = tile.get(i).tag
            var isBlank :Boolean = tile.get(i).tag == notTile
            val notValid = arrayOf(6,7,14,15,22,23,30,31,38,39,46,47,54,55)
            val list = asList(*notValid)
            if(!list.contains(i)){
                var x = i

                if (tile.get(x++).tag as Int == chosenTile && !isBlank && tile.get(x++).tag as Int == chosenTile && tile.get(x).tag as Int == chosenTile){
                    score = score + 3
                    scoreResult.text = "$score"
                    tile.get(x).setImageResource(notTile)
                    tile.get(x).setTag(notTile)
                    x--
                    tile.get(x).setImageResource(notTile)
                    tile.get(x).setTag(notTile)
                    x--
                    tile.get(x).setImageResource(notTile)
                    tile.get(x).setTag(notTile)
                }
            }
        }
        moveDownTiles()
    }

    private fun checkColumnForThree(){
        for(i in 0..47){
            var chosenTile = tile.get(i).tag
            var isBlank :Boolean = tile.get(i).tag == notTile
            var x = i

                if (tile.get(x).tag as Int == chosenTile && !isBlank && tile.get(x+noOfTile).tag as Int == chosenTile && tile.get(x+2*noOfTile).tag as Int == chosenTile){
                    score = score + 3
                    scoreResult.text = "$score"
                    tile.get(x).setImageResource(notTile)
                    tile.get(x).setTag(notTile)
                    x = x + noOfTile
                    tile.get(x).setImageResource(notTile)
                    tile.get(x).setTag(notTile)
                    x = x + noOfTile
                    tile.get(x).setImageResource(notTile)
                    tile.get(x).setTag(notTile)
                }

        }
        moveDownTiles()
    }

    private fun moveDownTiles() {
        val firstRow = arrayOf(1,2,3,5,6,7)
        val list = asList(*firstRow)
        for (i in 55 downTo 0){
            if (tile.get(i+noOfTile).tag as Int == notTile){

                tile.get(i+noOfTile).setImageResource(tile.get(i).tag as Int)
                tile.get(i+noOfTile).setTag(tile.get(i).tag as Int)

                tile.get(i).setImageResource(notTile)
                tile.get(i).setTag(notTile)
                if (list.contains(i) && tile.get(i).tag == notTile){
                    var randomTile :Int = Math.abs(Math.random() * tiles.size).toInt()
                    tile.get(i).setImageResource(tiles[randomTile])
                    tile.get(i).setTag(tiles[randomTile])
                }
            }
        }
        for(i in 0..7){
            if (tile.get(i).tag as Int == notTile){
                var randomTile :Int = Math.abs(Math.random() * tiles.size).toInt()
                tile.get(i).setImageResource(tiles[randomTile])
                tile.get(i).setTag(tiles[randomTile])
            }
        }
    }

    val repeatCheckers :Runnable = object :Runnable{
        override fun run() {
            try {
                checkRowForThree()
                checkColumnForThree()
                moveDownTiles()
            }
            finally {
                mHandler.postDelayed(this,interval)
            }
        }
    }

    private fun startRepeat() {
        repeatCheckers.run()
    }

    private fun createBoard() {
        val gridLayout = findViewById<GridLayout>(R.id.board)
        gridLayout.rowCount = noOfTile
        gridLayout.columnCount = noOfTile
        gridLayout.layoutParams.width = widthOfScreen
        gridLayout.layoutParams.height = widthOfScreen

        for (i in 0 until noOfTile * noOfTile){
            val imageView = ImageView(this)
            imageView.id = i
            imageView.layoutParams = android.view.ViewGroup.LayoutParams(widthOfTile,widthOfTile)

            imageView.maxHeight = widthOfTile
            imageView.maxWidth = widthOfTile

            var random :Int = Math.floor(Math.random() * tiles.size).toInt()

            imageView.setImageResource(tiles[random])
            imageView.setTag(tiles[random])

            tile.add(imageView)
            gridLayout.addView(imageView)
        }
    }
}