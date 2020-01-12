package com.umbrellaapps.urdu.keyboard;


//class Promotions {
//
//    private Context mContext;
//    private RequestQueue mRequestQueue;
//    private String baseUrl = "http://askshams.com/promotions/api/v1/";
//    HashMap<String, String> promotionalAds = new HashMap<>();
////    private SessionManager sessionManager;

/*
    Promotions(Context context) {
        this.mContext = context;
        sessionManager = new SessionManager(context);
    }*/

/*
    boolean getCustomInterstitialAds() {
        mRequestQueue = Volley.newRequestQueue(mContext);

        // Interstitial Ads
        String url = baseUrl + "index.php?action=interstitial";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean status = response.getBoolean("status");
                            if (status) {
                                ArrayList<String> customInterstitial = new ArrayList<>();
                                JSONObject data = response.getJSONObject("data");
                                customInterstitial.add(data.getString("url"));
                                customInterstitial.add(data.getString("image"));
                                sessionManager.saveArray(customInterstitial, "CUSTOM_INTERSTITIAL");
                                sessionManager.setRequestCompleted(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", "onErrorResponse: " + error);
                sessionManager.setRequestCompleted(false);
            }
        });
        mRequestQueue.add(jsObjRequest);

        return sessionManager.isRequestCompleted();
    }*/
/*

    boolean getPromotionalAds() {
        mRequestQueue = Volley.newRequestQueue(mContext);
        // Promotional Ads
        String url = baseUrl + "index.php?action=promotional";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean status = response.getBoolean("status");
                            if (status) {
                                JSONArray data = response.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = (JSONObject) data.get(i);
                                    promotionalAds.put(obj.getString("url"), obj.getString("image"));
                                }
                                sessionManager.saveMap(promotionalAds, "PROMOTIONAL_ADS");
                                sessionManager.setRequestCompleted(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", "onErrorResponse: " + error);
                sessionManager.setRequestCompleted(false);
            }
        });
        mRequestQueue.add(jsObjRequest);

        return sessionManager.isRequestCompleted();
    }
}
*/
