package com.yoon.lactosefree.brand

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class BrandViewModel : ViewModel() {

    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionNameBrand = "Brand"
    private val fireStoreCollectionNameBrandBeverage = "BrandBeverage"
    val coroutineScopeIO = CoroutineScope(Dispatchers.IO)

    private var _brand = MutableStateFlow<List<Brand>?>(null)
    val brand : StateFlow<List<Brand>?>
        get() = _brand

    private var _brandBeverage = MutableStateFlow<List<BrandBeverage>?>(null)
    val brandBeverage : StateFlow<List<BrandBeverage>?>
        get() = _brandBeverage

    private var _brandBeverageCategory = MutableStateFlow<List<String>?>(null)
    val brandBeverageCategory :  StateFlow<List<String>?>
        get() = _brandBeverageCategory

    private var _brandInsteadMilk = MutableStateFlow<List<Brand>?>(null)
    val brandInsteadMilk :  StateFlow<List<Brand>?>
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


    /**
     * BrandFragment에 주기용
     * 1. 일단 브랜드 객체를 얻는다.
     * 2. 브랜드 객체에서의 name으로 이미지를 찾는다.
     * 3. 두 개의 값을 더해서 하나의 클래스에 묶어 value에 더한다.
     */
     fun getBrand(){
        val tempList : MutableList<Brand> = mutableListOf()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        coroutineScopeIO.launch{
            val docRef: MutableList<GetBrandInfoFromFirebase> = fireStoreDB.collection(fireStoreCollectionNameBrand)
                .get().await().toObjects(GetBrandInfoFromFirebase::class.java)
            for (item in docRef){
                val pathReference = storageRef.child("Brand/BrandLogo/${item.brandName}.png")
                pathReference.downloadUrl
                    .addOnSuccessListener {
                    Log.e("GetBrand",it.toString())
                    tempList.add(
                        Brand(
                            brandName = item.brandName,
                            brandLogoImage = it)
                    )
                }.addOnCanceledListener {
                    Log.e("GetBrand","failed")
                }
            }
            flow<Brand> {
                _brand.emit(tempList)
            }.stateIn(CoroutineScope(Dispatchers.IO))
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun setBrandBeverage(brandBeverageName : String, boolean: Boolean){
        coroutineScopeIO.launch {
            val docRef = fireStoreDB.collection(fireStoreCollectionNameBrandBeverage).document(brandBeverageName)
                docRef.update("favorite",boolean)
                    .addOnSuccessListener {Log.d("TAG", "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener {Log.d("TAG", "DocumentSnapshot successfully updated!") }
        }
    }


    /**
     * 해당하는 브랜드 대체우유 정보를 가져오는 함수
     *
     * @param brandName
     */
    fun getIncludeMilk(brandName: String){
        val tempList : MutableList<Brand> = mutableListOf()
        coroutineScopeIO.launch {
            val docRef: MutableList<GetBrandInfoFromFirebase> = fireStoreDB.collection(fireStoreCollectionNameBrand)
                .whereEqualTo("brandName",brandName)
                .get().await().toObjects(GetBrandInfoFromFirebase::class.java)
            for(item in docRef){
                tempList.add(
                    Brand(
                        brandName = item.brandName,
                        insteadMilk = item.insteadMilk)
                )
            }
            flow<Brand> {
                _brandInsteadMilk.emit(tempList)
            }.stateIn(CoroutineScope(Dispatchers.IO))
        }
    }

    /**
     * 해당하는 브랜드의 음료를 탭에 맞게 불러오는 함수
     *
     * @param brandName 해당하는 브랜드 이름
     * @param category  탭을 눌렀을 때 카테고리
     */
    fun getBrandBeverage(brandName : String, category :String){
        val tempList : MutableList<BrandBeverage> = mutableListOf()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        coroutineScopeIO.launch{
            val docRef: MutableList<GetBrandBeverageInfoFromFirebase> = fireStoreDB
                .collection(fireStoreCollectionNameBrandBeverage)
                .whereEqualTo("beverageCategory", category)
                .get().await().toObjects(GetBrandBeverageInfoFromFirebase::class.java)
            for (item in docRef){
                Log.e("testItem",item.beverageName)
                val pathReference = storageRef.child("Beverage/${brandName}/${item.beverageName}.png")
                pathReference.downloadUrl
                    .addOnSuccessListener {imageUri ->
                        Log.e("get","success")
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
                _brandBeverage.emit(tempList)
                Log.e("TagVMemit","success")
            }.stateIn(CoroutineScope(Dispatchers.IO))
        }
    }

    /**
     * 해당하는 브랜드의 음료 카테고리를 가져오는 함수
     *
     * @param brandName 브랜드 이름
     */
    fun getBrandCategory(brandName: String){
        coroutineScopeIO.launch{
            val tempSet : MutableSet<String> = mutableSetOf()
            val docRef: MutableList<GetBrandBeverageInfoFromFirebase> = fireStoreDB
                .collection(fireStoreCollectionNameBrandBeverage)
                .whereEqualTo("brandName",brandName)
                .get().await().toObjects(GetBrandBeverageInfoFromFirebase::class.java)
            for (item in docRef){
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
    fun setFavoriteBeverage(beverage : BrandBeverage){
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

    fun deleteFavoriteBeverage(beverageName : String){
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
    val includeMilk : Boolean = false,
    var favorite : Boolean = false
)