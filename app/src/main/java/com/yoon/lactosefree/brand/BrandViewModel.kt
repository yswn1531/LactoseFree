package com.yoon.lactosefree.brand

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.yoon.lactosefree.common.BRAND_IMAGE_STORAGE_PATH
import com.yoon.lactosefree.common.DefaultApplication
import com.yoon.lactosefree.common.FIRESTORE_COLLECTION_NAME_BEVERAGE
import com.yoon.lactosefree.common.FIRESTORE_COLLECTION_NAME_BRAND
import com.yoon.lactosefree.favorite.Favorite
import com.yoon.lactosefree.favorite.FavoriteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class BrandViewModel : ViewModel() {

    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionNameBrand = FIRESTORE_COLLECTION_NAME_BRAND
    private val fireStoreCollectionNameBrandBeverage = FIRESTORE_COLLECTION_NAME_BEVERAGE

    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)
    private val coroutineScopeIOBrand = CoroutineScope(Dispatchers.IO)

    private val isBrandImageDownload = MutableStateFlow(false)
    private val isBrandBeverageImageDownload = MutableStateFlow(false)


    private var _brand = MutableStateFlow<List<Brand>?>(null)
    val brand: StateFlow<List<Brand>?>
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


    private var brandImageMap: MutableMap<String, Uri> = mutableMapOf()
    private var beverageImageMap: MutableMap<String, Uri> = mutableMapOf()
    private var isDownloadBrandTask : Boolean = false
    private var isDownloadBeverageTask : Boolean = false

    fun getBrandImageFromStorage() {
        if (!isDownloadBrandTask){
            coroutineScopeIO.launch {
                val pathReference =
                    FirebaseStorage.getInstance().reference.child(BRAND_IMAGE_STORAGE_PATH)
                        .listAll().await()
                val result = coroutineScopeIO.async {
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
                    Log.e("BRAND-IMAGE-GET", "DONE")
                    isBrandImageDownload.value = true
                    isDownloadBrandTask = true
                }
            }
        }
    }

    fun getBrandBeverageImageFromStorage() {
        if (!isDownloadBeverageTask) {
            coroutineScopeIO.launch {
                val pathReference =
                    FirebaseStorage.getInstance().reference.child("Beverage/스타벅스/").listAll()
                        .await()
                val result = coroutineScopeIO.async {
                    pathReference.items.let {
                        for (item in it) {
                            item.downloadUrl.addOnSuccessListener { uri ->//이게 비동기라.
                                beverageImageMap[item.name.getOnlyName()] = uri
                            }.addOnFailureListener {
                                Log.e("STORAGE", "Storage Error")
                            }.await()
                        }
                    }
                    beverageImageMap
                }.await()
                if (result.isNotEmpty()) {
                    isBrandBeverageImageDownload.value = true
                    isDownloadBeverageTask = true
                }
            }
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
            _brandInsteadMilk.emit(tempList)
        }
    }


    /**
     * Brand에 대한 정보를 얻는 함수
     *
     */
    fun getBrand() {
        Log.e("STARTBRAND", "STARTBRAND")
        coroutineScopeIO.launch {
            isBrandImageDownload.collect { isDownload ->
                if (isDownload) {
                    val tempList: MutableList<Brand> = mutableListOf()
                    val docRef: MutableList<GetBrandInfoFromFirebase> =
                        fireStoreDB.collection(fireStoreCollectionNameBrand)
                            .get().await().toObjects(GetBrandInfoFromFirebase::class.java)
                    val result = coroutineScopeIO.async {
                        docRef.asSequence().forEach { data ->
                            brandImageMap
                                .filter { it.key == data.brandName }
                                .forEach { mapItem ->
                                    tempList.add(
                                        Brand(
                                            brandName = data.brandName,
                                            brandLogoImage = mapItem.value
                                        )
                                    )
                                }
                        }
                        tempList
                    }.await()
                    if (result.isNotEmpty()) {
                        _brand.emit(result)
                        Log.e("EMIT-BRAND", "EMIT-BRAND")
                    }
                }
            }
        }
    }


    /* fun getBrand() {
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
     * 해당하는 브랜드의 음료를 탭에 맞게 불러오는 함수
     *
     * @param category  탭을 눌렀을 때 카테고리
     */
    fun getBrandBeverage(category: String) {
        Log.e("STARTBEVERAGE", "STARTBEVERAGE")
        coroutineScopeIO.launch {
            isBrandBeverageImageDownload.collect { isDownload ->
                Log.e("isDownload-COLLECT", isDownload.toString())
                if (isDownload) {
                    val tempList: MutableList<BrandBeverage> = mutableListOf()
                    val docRef: MutableList<GetBrandBeverageInfoFromFirebase> = fireStoreDB
                        .collection(fireStoreCollectionNameBrandBeverage)
                        .whereEqualTo("beverageCategory", category)
                        .get().await().toObjects(GetBrandBeverageInfoFromFirebase::class.java)
                    val result = coroutineScopeIO.async {
                        docRef.forEach {beverages ->
                            beverageImageMap
                                .filter { beverages.beverageName == it.key }
                                .forEach { images ->
                                    tempList.add(
                                        BrandBeverage(
                                            brandName = beverages.brandName,
                                            beverageName = beverages.beverageName,
                                            beverageCategory = beverages.beverageCategory,
                                            beverageSize = beverages.beverageSize,
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
                        tempList
                    }.await()
                    if (result.isNotEmpty()) {
                        Log.e("BEVERAGE-EMIT", "BEVERAGE-EMIT")
                        flow<List<BrandBeverage>> {
                            _brandBeverage.emit(tempList)
                        }.stateIn(coroutineScopeIOBrand)
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

    private val repository: FavoriteRepository =
        FavoriteRepository(DefaultApplication.applicationContext())

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
    }

    fun deleteFavoriteBeverage(beverageName: String) {
        repository.deleteFavorite(beverageName)
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
}

data class GetBrandInfoFromFirebase(
    val brandName: String = "",
    val insteadMilk: MutableList<String> = mutableListOf()
)

data class GetBrandBeverageInfoFromFirebase(
    val brandName: String = "",
    val beverageName: String = "",
    val beverageCategory: String = "",
    val beverageSize: Int = 0,
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