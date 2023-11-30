package com.yoon.lactosefree.brand

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.yoon.lactosefree.common.DefaultApplication
import com.yoon.lactosefree.common.FIRESTORE_COLLECTION_NAME_BEVERAGE
import com.yoon.lactosefree.common.FIRESTORE_COLLECTION_NAME_BRAND
import com.yoon.lactosefree.favorite.Favorite
import com.yoon.lactosefree.favorite.FavoriteRepository
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.nio.channels.Channel


class BrandViewModel : ViewModel() {

    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionNameBrand = FIRESTORE_COLLECTION_NAME_BRAND
    private val fireStoreCollectionNameBrandBeverage = FIRESTORE_COLLECTION_NAME_BEVERAGE
    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)

    private val coroutineScopeIOBrand = CoroutineScope(Dispatchers.IO)

    val flow : Flow<Boolean> = emptyFlow()
    val isBrandImageFetched = MutableStateFlow(false)
    val isBrandBeverageImageFetched = MutableStateFlow(false)

    private val repository: FavoriteRepository =
        FavoriteRepository(DefaultApplication.applicationContext())


    private var _brand = MutableLiveData<List<Brand>?>(null)
    val brand: LiveData<List<Brand>?>
        get() = _brand

    private var _brandBeverage = MutableStateFlow<List<BrandBeverage>?>(null)
    val brandBeverage: StateFlow<List<BrandBeverage>?>
        get() = _brandBeverage

    private var _brandBeverageCategory = MutableStateFlow<List<String>?>(null)
    val brandBeverageCategory: StateFlow<List<String>?>
        get() = _brandBeverageCategory

    private var _brandInsteadMilk = MutableStateFlow<List<Brand>?>(null)
    val brandInsteadMilk: StateFlow<List<Brand>?>
        get() = _brandInsteadMilk


    /* private var _brandBeverageOrderBySugar = MutableStateFlow<List<BrandBeverage>?>(null)
     val brandBeverageOrderBySugar : StateFlow<List<BrandBeverage>?>
         get() = _brandBeverageOrderBySugar

     private var _brandBeverageOrderByKcal = MutableStateFlow<List<BrandBeverage>?>(null)
     val brandBeverageOrderByKcal : StateFlow<List<BrandBeverage>?>
         get() = _brandBeverageOrderByKcal

     private var _brandBeverageOrderByCaffeine = MutableStateFlow<List<BrandBeverage>?>(null)
     val brandBeverageOrderByCaffeine : StateFlow<List<BrandBeverage>?>
         get() = _brandBeverageOrderByCaffeine*/


    private var brandImageMap: MutableMap<String, Uri> = mutableMapOf()
    private var beverageImageMap: MutableMap<String, Uri> = mutableMapOf()

    fun getBrandImageFromStorage() {
        coroutineScopeIOBrand.launch {
            val pathReference =
                FirebaseStorage.getInstance().reference.child("Brand/BrandLogo/").listAll().await()
            val result = coroutineScopeIOBrand.async {
                pathReference.items.let {
                    for (item in it) {
                        item.downloadUrl.addOnSuccessListener {     //이게 비동기라.
                            brandImageMap[item.name.getOnlyName()] = it
                        }.addOnFailureListener {
                            Log.e("STORAGE", "Storage Error")
                        }.await()
                    }
                }
                brandImageMap
            }.await()
            if (result.isNotEmpty()) {
                Log.e("GET", "DONE")
                isBrandImageFetched.value = true
            }
        }
    }

    fun getBrandBeverageImageFromStorage(brandName: String) {
        coroutineScopeIOBrand.launch {
            val pathReference =
                FirebaseStorage.getInstance().reference.child("Beverage/$brandName/").listAll()
                    .await()
            val result = coroutineScopeIOBrand.async {
                pathReference.items.let {
                    for (item in it) {
                        item.downloadUrl.addOnSuccessListener {     //이게 비동기라.
                            beverageImageMap[item.name.getOnlyName()] = it
                        }.addOnFailureListener {
                            Log.e("STORAGE", "Storage Error")
                        }.await()
                    }
                }
                brandImageMap
            }.await()
            if (result.isNotEmpty()) {
                Log.e("BEVERAGE-GET", "DONE")
                isBrandBeverageImageFetched.value = true
            }
        }
    }

    init {
        getBrandImageFromStorage()
    }

    /**
     * Brand에 대한 정보를 얻는 함수
     *
     */
    fun getBrand() {
        Log.e("STARTBRAND", "STARTBRAND")
        coroutineScopeIO.launch {
            isBrandImageFetched.collect { isFetched ->
                if (isFetched) {
                    val tempList: MutableList<Brand> = mutableListOf()
                    val docRef: MutableList<GetBrandInfoFromFirebase> =
                        fireStoreDB.collection(fireStoreCollectionNameBrand)
                            .get().await().toObjects(GetBrandInfoFromFirebase::class.java)
                    val result = coroutineScopeIO.async {
                        Log.e("STARTGET", "STARTGET")
                        for (data in docRef) {
                            for (mapItem in brandImageMap) {
                                if (data.brandName == mapItem.key) {
                                    Log.e("INPUT", "${data.brandName} , ${mapItem.key}")
                                    tempList.add(
                                        Brand(
                                            brandName = data.brandName,
                                            brandLogoImage = mapItem.value
                                        )
                                    )
                                } else {
                                    Log.e("OUTPUT", "${data.brandName} , ${mapItem.key}")
                                }
                            }
                        }
                        tempList
                    }.await()
                    if (result.isNotEmpty()) {
                        Log.e("EMIT", "EMIT")
                        _brand.postValue(result)
                    }
                }
            }
        }
    }


    /*fun getBrand() {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        coroutineScopeIO.launch {
            val tempList: MutableList<Brand> = mutableListOf()
            val docRef: MutableList<GetBrandInfoFromFirebase> =
                fireStoreDB.collection(fireStoreCollectionNameBrand)
                    .get().await().toObjects(GetBrandInfoFromFirebase::class.java)
            val result = coroutineScopeIO.async {
                for (item in docRef) {
                    val pathReference = storageRef.child("Brand/BrandLogo/${item.brandName}.png")
                    pathReference.downloadUrl
                        .addOnSuccessListener {
                            tempList.add(
                                Brand(
                                    brandName = item.brandName,
                                    brandLogoImage = it
                                )
                            )
                        }.addOnCanceledListener {
                            Log.e("GetBrand", "failed")
                        }.await()
                }
                tempList
            }.await()
            if (result.isNotEmpty()) {
                _brand.emit(result)
            }
        }
    }*/


    /**
     * favorite 여부 변경 함수
     *
     * @param brandBeverageName
     * @param boolean
     */
    fun setBrandBeverage(brandBeverageName: String, boolean: Boolean) {
        coroutineScopeIO.launch {
            val docRef = fireStoreDB.collection(fireStoreCollectionNameBrandBeverage)
                .document(brandBeverageName)
            docRef.update("favorite", boolean)
                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
        }
    }


    /**
     * 해당하는 브랜드 대체우유 정보를 가져오는 함수
     *
     * @param brandName
     */
    fun getIncludeMilk(brandName: String) {
        Log.e("STARTMILK", "STARTMILK")
        val tempList: MutableList<Brand> = mutableListOf()
        coroutineScopeIO.launch {
            val docRef: MutableList<GetBrandInfoFromFirebase> =
                fireStoreDB.collection(fireStoreCollectionNameBrand)
                    .whereEqualTo("brandName", brandName)
                    .get().await().toObjects(GetBrandInfoFromFirebase::class.java)
            for (item in docRef) {
                tempList.add(
                    Brand(
                        brandName = item.brandName,
                        insteadMilk = item.insteadMilk
                    )
                )
            }
            Log.e("MILKVM", "MILKVM")
            _brandInsteadMilk.emit(tempList)
        }
    }

    /**
     * 해당하는 브랜드의 음료를 탭에 맞게 불러오는 함수
     *
     * @param brandName 해당하는 브랜드 이름
     * @param category  탭을 눌렀을 때 카테고리
     */
    fun getBrandBeverage(category: String) {
        Log.e("STARTBEVERAGE", "STARTBEVERAGE")
        coroutineScopeIO.launch {
            isBrandBeverageImageFetched.collect { isFetched ->
                if (isFetched){
                    val tempList: MutableList<BrandBeverage> = mutableListOf()
                    val docRef: MutableList<GetBrandBeverageInfoFromFirebase> = fireStoreDB
                        .collection(fireStoreCollectionNameBrandBeverage)
                        .whereEqualTo("beverageCategory", category)
                        .get().await().toObjects(GetBrandBeverageInfoFromFirebase::class.java)
                    val result = coroutineScopeIO.async {
                        for (beverages in docRef) {
                            for (images in beverageImageMap) {
                                if (beverages.beverageName == images.key) {
                                    tempList.add(
                                        BrandBeverage(
                                            brandName = beverages.brandName,
                                            beverageName = beverages.beverageName,
                                            beverageCategory = beverages.beverageCategory,
                                            beverageKcal = beverages.beverageKcal,
                                            beverageCaffeine = beverages.beverageCaffeine,
                                            beverageFat = beverages.beverageFat,
                                            beverageProtein = beverages.beverageProtein,
                                            beverageSodium = beverages.beverageSodium,
                                            beverageSugar = beverages.beverageSugar,
                                            beverageImage = images.value,
                                            favorite = beverages.favorite,
                                            includeMilk = beverages.includeMilk
                                        )
                                    )
                                }
                            }
                        }
                        tempList
                    }.await()
                    if (result.isNotEmpty()) {
                        Log.e("BEVERAGE-EMIT", "BEVERAGE-EMIT")
                        _brandBeverage.emit(tempList)
                    }
                }
            }
        }
    }

    /**
     * 해당하는 브랜드의 음료 카테고리를 가져오는 함수
     *
     * @param brandName 브랜드 이름
     */
    fun getBrandCategory(brandName: String) {
        coroutineScopeIO.launch {
            val tempSet: MutableSet<String> = mutableSetOf()
            val docRef: MutableList<GetBrandBeverageInfoFromFirebase> = fireStoreDB
                .collection(fireStoreCollectionNameBrandBeverage)
                .whereEqualTo("brandName", brandName)
                .get().await().toObjects(GetBrandBeverageInfoFromFirebase::class.java)
            for (item in docRef) {
                tempSet.add(item.beverageCategory)
            }
            flow<BrandBeverage> {
                _brandBeverageCategory.emit(tempSet.toList())
            }.stateIn(CoroutineScope(Dispatchers.IO))
        }
    }


    /*fun getBrandBeverageOrderByCaffeine(brandName : String, category :String){
        val tempList : MutableList<BrandBeverage> = mutableListOf()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        CoroutineScope(Dispatchers.IO).launch{
            val docRef: MutableList<GetBrandBeverageInfoFromFirebase> = fireStoreDB
                .collection(fireStoreCollectionNameBrandBeverage)
                .whereEqualTo("beverageCategory", category)
                .whereEqualTo("beverageCaffeine", 0)
                .get().await().toObjects(GetBrandBeverageInfoFromFirebase::class.java)
            for (item in docRef){
                Log.e("testItem",item.beverageName)
                val pathReference = storageRef.child("Beverage/${brandName}/${item.beverageName}.png")
                pathReference.downloadUrl
                    .addOnSuccessListener {imageUri ->
                        Log.e("TagVM","sucees")
                        tempList.add(
                            BrandBeverage(
                                brandName = item.brandName,
                                beverageName = item.beverageName,
                                beverageCategory = item.beverageCategory,
                                beverageKcal = item.beverageKcal,
                                beverageCaffeine = item.beverageCaffeine,
                                beverageFat = item.beverageFat,
                                beverageProtein = item.beverageProtein,
                                beverageSodium = item.beverageSodium,
                                beverageSugar = item.beverageSugar,
                                beverageImage = imageUri,
                                favorite = item.favorite,
                                includeMilk = item.includeMilk
                            )
                        )
                    }.addOnCanceledListener {
                        Log.e("GetBrand","failed")
                    }
            }
            flow<BrandBeverage> {
                _brandBeverageOrderByCaffeine.emit(tempList)
            }.stateIn(CoroutineScope(Dispatchers.IO))
        }
    }

    fun getBrandBeverageOrderBySugar(brandName : String, category :String){
        val tempList : MutableList<BrandBeverage> = mutableListOf()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        CoroutineScope(Dispatchers.IO).launch{
            val docRef: MutableList<GetBrandBeverageInfoFromFirebase> = fireStoreDB
                .collection(fireStoreCollectionNameBrandBeverage)
                .whereEqualTo("beverageCategory", category)
                .whereEqualTo("beverageCaffeine", 0)
                .get().await().toObjects(GetBrandBeverageInfoFromFirebase::class.java)
            for (item in docRef){
                Log.e("testItem",item.beverageName)
                val pathReference = storageRef.child("Beverage/${brandName}/${item.beverageName}.png")
                pathReference.downloadUrl
                    .addOnSuccessListener {imageUri ->
                        Log.e("TagVM","sucees")
                        tempList.add(
                            BrandBeverage(
                                brandName = item.brandName,
                                beverageName = item.beverageName,
                                beverageCategory = item.beverageCategory,
                                beverageKcal = item.beverageKcal,
                                beverageCaffeine = item.beverageCaffeine,
                                beverageFat = item.beverageFat,
                                beverageProtein = item.beverageProtein,
                                beverageSodium = item.beverageSodium,
                                beverageSugar = item.beverageSugar,
                                beverageImage = imageUri,
                                favorite = item.favorite,
                                includeMilk = item.includeMilk
                            )
                        )
                    }.addOnCanceledListener {
                        Log.e("GetBrand","failed")
                    }
            }
            flow<BrandBeverage> {
                _brandBeverageOrderBySugar.emit(tempList)
            }.stateIn(CoroutineScope(Dispatchers.IO))
        }
    }*/


    /**
     *  Favorite Image Button 클릭시 브랜드 이미지,
     *
     */
    fun insertFavoriteBeverage(beverage: BrandBeverage) {
        repository.insertFavorite(
            Favorite(
                brandName = beverage.brandName,
                brandBeverageName = beverage.beverageName,
                imageUri = beverage.beverageImage
            )
        )
        /**
         *
         * insertFavorite(
         *      Favorite(
         *      beverage.brandName = ""
         *      beverage.beverageName = ""
         *      raiting detail에서 수정
         *      content detail에서 수정
         *      )
         *  )
         */
    }

    fun deleteFavoriteBeverage(beverageName: String) {
        repository.deleteFavorite(beverageName)
        /**
         *  deleteFavorite(beverageName)
         */
    }


}

data class GetBrandInfoFromFirebase(
    val brandName: String = "",
    val insteadMilk: MutableList<String> = mutableListOf()
)

data class GetBrandBeverageInfoFromFirebase(
    val brandName: String = "",
    val beverageName: String = "",
    val beverageCategory: String = "",
    val beverageKcal: Float = 0.0f,
    val beverageProtein: Float = 0.0f,
    val beverageSugar: Float = 0.0f,
    val beverageSodium: Float = 0.0f,
    val beverageFat: Float = 0.0f,
    val beverageCaffeine: Float = 0.0f,
    val includeMilk: Boolean = false,
    var favorite: Boolean = false
)

fun String.getOnlyName(): String {
    return this.split(".")[0]
}