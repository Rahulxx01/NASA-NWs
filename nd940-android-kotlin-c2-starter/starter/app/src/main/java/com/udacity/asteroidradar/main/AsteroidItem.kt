package com.udacity.asteroidradar.main

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.list_asteroid_item.*

class AsteroidItem(
    val asteriod : Asteroid
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            asteroidNameTextView.text = asteriod.codename
            asteroidDateTextView.text = asteriod.closeApproachDate
        }
    }

    override fun getLayout() = R.layout.list_asteroid_item


}