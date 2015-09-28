
#ILoveMarshmallow
This app was created for a Zappos Android Challenge.   
You can find Signed App [here](https://github.com/ojuneja/ILoveMarshMallow/blob/master/release_apk/ILoveMarshmallow.apk?raw=true)

####APP Description:
1.  Takes input from the user for the search query(using the search endpoint).
2.  Parse JSON and load the results on the page into a recycler view.
3.  Clicking on any of the products show the product details.
4.  “Share” the product page to a friend who has your app running on their phone such that they are able to view the same product.

####API Used:
1. The following URL/endpoint/web service can be used to get back search results.
   https://zappos.amazon.com/mobileapi/v1/search?term= search term here>  

  Example: https://zappos.amazon.com/mobileapi/v1/search?term=adidas
2. Product Information endpoint - Use the asin that is returned from the search results to load product information.
   https://zappos.amazon.com/mobileapi/v1/product/asin/?

  Example: https://zappos.amazon.com/mobileapi/v1/product/asin/B00LLS8QV0
  
####Libraries Used:
- Material Design - RecyclerView,CardView,Android Design Support Library
- API Request and Response - Async Taks,HTTP
- Image Loader: LruBitmapCache
- To show description in proper way - [HTMLTextView](https://github.com/SufficientlySecure/html-textview)
- For Animation - [Animator](https://github.com/wasabeef/recyclerview-animators)


####Screenshots

<img src=https://github.com/ojuneja/ILoveMarshMallow/blob/master/Screenshots/Homepage.png width="250"/> 
<img src=https://github.com/ojuneja/ILoveMarshMallow/blob/master/Screenshots/ProductListPage.png width="250"/> 
<img src=https://github.com/ojuneja/ILoveMarshMallow/blob/master/Screenshots/Loading.png width="250"/> 
<img src=https://github.com/ojuneja/ILoveMarshMallow/blob/master/Screenshots/ProductInformationPage.png width="250"/>
<img src=https://github.com/ojuneja/ILoveMarshMallow/blob/master/Screenshots/Share.png width="250"/>

