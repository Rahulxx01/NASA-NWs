package com.udacity.asteroidradar.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.NextSevenDaysActivity
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.AsteriodAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.repository.MyWorker
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainFragment : ScopedFragment(),KodeinAware , AsteriodAdapter.OnNoteListener{


    override val kodein by closestKodein()


    private var asteroidDatabase: AsteroidDatabase? = null

    lateinit var request : OneTimeWorkRequest
    private val viewModelFactory : AsteroidViewModelFactory by instance()

   private var mAsteroidList : List<Asteroid> = emptyList()
    var navController: NavController? = null
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)
    }
    var dateArrayList = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)

        //WorkManager
        var request  = OneTimeWorkRequest.Builder(MyWorker::class.java).build()

        WorkManager.getInstance().enqueue(request)


        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        navController = view?.let { Navigation.findNavController(it) }
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


//        val apiService = ImageApiService(ConnectivityInterceptorImpl(this.context!!))
//
//        val imageNetworkDataSoruce = ImageNetworkDataSourceImpl(apiService)
//
//        imageNetworkDataSoruce.downloadImage.observe(viewLifecycleOwner , Observer {
//            Picasso.get().load(it.url).into(activity_main_image_of_the_day)
//            Toast.makeText(context,it.url,Toast.LENGTH_SHORT).show();
//        })
//
//        GlobalScope.launch(Dispatchers.Main) {
//            val imageResponse = apiService.getImageData().await()
//           // Picasso.get().load(imageResponse.url).into(activity_main_image_of_the_day)
//           // Toast.makeText(context,imageResponse.url,Toast.LENGTH_SHORT).show();
//
//            imageNetworkDataSoruce.fetechImage()
//
//        }
        request  = OneTimeWorkRequest.Builder(MyWorker::class.java).build()

        var workManager = WorkManager.getInstance()

        workManager.enqueue(request)

        bindImage()


      WorkManager.getInstance().getWorkInfoByIdLiveData(request.id).observe(viewLifecycleOwner, Observer {


          val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
          val now: LocalDateTime = LocalDateTime.now()
          asteroidDatabase = context?.let { AsteroidDatabase.invoke(it) };

          asteroid_recycler.setLayoutManager(GridLayoutManager(context, 2))

          val asteroidFinalList =  asteroidDatabase!!.asteroidDao().getAsteroidData(now.toString())

          asteroid_recycler.adapter = context?.let {
              asteroidFinalList.value?.let { it1 ->
                  AsteriodAdapter(
                      it1,activity!!.applicationContext,this@MainFragment)
              }
          }
      })

        bindUI()





    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.show_seven_days_working_manager -> {
                val intent = Intent(context, NextSevenDaysActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    public fun bindUI() = launch(Dispatchers.Main){

        val currentAsteroid = viewModel.asteroidList.await()

        currentAsteroid.observe(viewLifecycleOwner, Observer { asteroidList ->
            if(asteroidList==null){
                return@Observer
            }
            Log.d("Data",asteroidList.toString())
            mAsteroidList = asteroidList
            asteroid_recycler.layoutManager = LinearLayoutManager(context)
            asteroid_recycler.adapter = context?.let { AsteriodAdapter(
                asteroidList,activity!!.applicationContext,this@MainFragment) }


        })

    }

    fun bindUIVersion(){


        GlobalScope.launch(Dispatchers.Main) {
            val currentAsteroid = viewModel.asteroidList.await()

            currentAsteroid.observe(viewLifecycleOwner, Observer { asteroidList ->
                if(asteroidList==null){
                    return@Observer
                }
                Log.d("Data",asteroidList.toString())
                mAsteroidList = asteroidList
                asteroid_recycler.layoutManager = LinearLayoutManager(context)
                asteroid_recycler.adapter = context?.let { AsteriodAdapter(
                    asteroidList,activity!!.applicationContext,this@MainFragment) }


            })

        }

    }


    public fun bindImage() = launch{
        val currentImage = viewModel.imageNASA.await()
        currentImage.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer

            if(it.media_type == "image"){
                Picasso.get().load(it.url).into(activity_main_image_of_the_day)
            }

          Toast.makeText(context,it.url,Toast.LENGTH_SHORT).show();
        })
    }



    override fun onNoteClick(position: Int) {
        
        mAsteroidList.get(position)
        Log.d("Position",position.toString())
//        navController!!.navigate(R.id.action_showDetail)
    }

    class WorkManagerClass(context: Context, workerParams: WorkerParameters) : Worker(context,
        workerParams
    ){
        val mainFragment = MainFragment()
        override fun doWork(): Result {
            mainFragment.bindUI()
            return Result.success()
        }

    }


}
