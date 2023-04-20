package com.bbw.restapp.controller;

import com.bbw.restapp.exception.EntryNotFoundException;
import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.query.IdQuery;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openspaces.core.GigaSpace;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;


@RestController
public class RetentionPolicyController {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Resource
    private GigaSpace gigaSpace;


    @GetMapping("/message")
    public String displayMessage(){
        return "Congratulation, you successfully deployed your application to kubernetes !!";
    }
    @GetMapping(value = "/stores", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getStores() {
        JSONArray resultJsonArr = new JSONArray();
        getAllStores(resultJsonArr);
        return resultJsonArr.toString();
    }

    @GetMapping(value = "/stores/{storeNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getStoresbyStorenumber(@PathVariable String storeNumber) {
        JSONObject resultJson = new JSONObject();
        getStorebyStoreNumber(storeNumber,resultJson);
        if(resultJson.isEmpty()){
            logger.info("Store with id ("+storeNumber+") not found");
            throw new EntryNotFoundException("Store with storeId ("+storeNumber+") not found");
        }
        return resultJson.toString();
    }

    @GetMapping(value = "/district", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDistricts() {
        JSONArray resultJsonArr = new JSONArray();
        getAllDistrict(resultJsonArr);
        return resultJsonArr.toString();
    }

    @GetMapping(value = "/district/{districtCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDistrictByCode(@PathVariable String districtCode) {
        JSONObject resultJson = new JSONObject();
        getDistrictbyDistrictCode(districtCode,resultJson);
        if(resultJson.isEmpty()){
            logger.info("District with code ("+districtCode+") not found");
            throw new EntryNotFoundException("District with code ("+districtCode+") not found");
        }
        return resultJson.toString();
    }

    @GetMapping(value = "/region", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getRegions() {
        JSONArray resultJsonArr = new JSONArray();
        getAllRegion(resultJsonArr);
        return resultJsonArr.toString();
    }

    @GetMapping(value = "/region/{regionCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getRegionByCode(@PathVariable String regionCode) {
        JSONObject resultJson = new JSONObject();
        getRegionbyRegionCode(regionCode,resultJson);
        if(resultJson.isEmpty()){
            logger.info("Region with code ("+regionCode+") not found");
            throw new EntryNotFoundException("Region with code ("+regionCode+") not found");
        }
        return resultJson.toString();
    }

    @GetMapping(value = "/zone", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getZones() {
        JSONArray resultJsonArr = new JSONArray();
        getAllZone(resultJsonArr);
        return resultJsonArr.toString();
    }

    @GetMapping(value = "/zone/{zoneCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getZoneByCode(@PathVariable String zoneCode) {
        JSONObject resultJson = new JSONObject();
        getZonebyZoneCode(zoneCode,resultJson);
        if(resultJson.isEmpty()){
            logger.info("Zone with code ("+zoneCode+") not found");
            throw new EntryNotFoundException("Zone with code ("+zoneCode+") not found");
        }
        return resultJson.toString();
    }

    public  JSONObject getStorebyStoreNumber(String storeNumber,JSONObject resultJson){
        SpaceDocument store = gigaSpace.readById(new IdQuery<>("stores", storeNumber));
        logger.info("Store Obj: "+store);
        if(store == null){
            return resultJson;
        }
        Map<String, Object> storeProperties = store.getProperties();
        Properties p = asProperties(storeProperties);
        resultJson.put("StoreDetails",p);
        getDistrictbyDistrictCode(String.valueOf(storeProperties.get("DistrictCode")),resultJson);
        return resultJson;
    }
    public  JSONObject getDistrictbyDistrictCode(String districtCode,JSONObject resultJson){
        SpaceDocument district = gigaSpace.readById(new IdQuery<>("district",districtCode));
        if(district == null){
            return resultJson;
        }
        Properties districtProp = asProperties(district.getProperties());
        resultJson.put("DistrictDetails",districtProp);
        getRegionbyRegionCode(String.valueOf(district.getProperties().get("RegionCode")),resultJson);
        return resultJson;
    }
    public  JSONObject getRegionbyRegionCode(String regionCode,JSONObject resultJson){
        SpaceDocument region = gigaSpace.readById(new IdQuery<>("region", regionCode));
        if(region == null){
            return resultJson;
        }
        Properties regionProp = asProperties(region.getProperties());
        resultJson.put("RegionDetails",regionProp);
        getZonebyZoneCode(String.valueOf(region.getProperties().get("ZoneCode")),resultJson);
        return resultJson;
    }
    public  JSONObject getZonebyZoneCode(String zoneCode,JSONObject resultJson){
        SpaceDocument zone = gigaSpace.readById(new IdQuery<>("zone", zoneCode));
        if(zone == null){
            return resultJson;
        }
        Properties zoneProp = asProperties(zone.getProperties());
        resultJson.put("ZoneDetails",zoneProp);
        return resultJson;
    }

    public  JSONArray getAllStores(JSONArray resultJsonArr){
        SpaceDocument template = new SpaceDocument("stores");
        SpaceDocument[] stores = gigaSpace.readMultiple(template);
        logger.info("Total stores count: " + stores.length);
        for(SpaceDocument store: stores){
            JSONObject json = new JSONObject();
            Map<String, Object> storeProperties = store.getProperties();
            Properties p = asProperties(storeProperties);
            json.put("StoreDetails",p);
            getDistrictbyDistrictCode(String.valueOf(storeProperties.get("DistrictCode")),json);
            resultJsonArr.put(json);
        }
        return resultJsonArr;
    }
    public  JSONArray getAllDistrict(JSONArray resultJsonArr){
        SpaceDocument template = new SpaceDocument("district");
        SpaceDocument[] districts = gigaSpace.readMultiple(template);
        logger.info("Total district count: " + districts.length);
        for(SpaceDocument district: districts){
            JSONObject json = new JSONObject();
            Properties p = asProperties(district.getProperties());
            json.put("DistrictDetails",p);
            getRegionbyRegionCode(String.valueOf(district.getProperties().get("RegionCode")),json);
            resultJsonArr.put(json);
        }
        return resultJsonArr;
    }

    public  JSONArray getAllRegion(JSONArray resultJsonArr){
        SpaceDocument template = new SpaceDocument("region");
        SpaceDocument[] regions = gigaSpace.readMultiple(template);
        logger.info("Total region count: " + regions.length);
        for(SpaceDocument region: regions){
            JSONObject json = new JSONObject();
            Properties p = asProperties(region.getProperties());
            json.put("RegionDetails",p);
            getZonebyZoneCode(String.valueOf(region.getProperties().get("ZoneCode")),json);
            resultJsonArr.put(json);
        }
        return resultJsonArr;
    }

    public  JSONArray getAllZone(JSONArray resultJsonArr){
        SpaceDocument template = new SpaceDocument("zone");
        SpaceDocument[] zones = gigaSpace.readMultiple(template);
        logger.info("Total zone count: " + zones.length);
        for(SpaceDocument zone: zones){
            JSONObject json = new JSONObject();
            Properties p = asProperties(zone.getProperties());
            json.put("ZoneDetails",p);
            resultJsonArr.put(json);
        }
        return resultJsonArr;
    }

    public static Properties asProperties(Map<String, ?> map) {
        Properties props = new Properties();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            Object v = entry.getValue();
            if (v != null) {
                props.put(entry.getKey(), v.toString());
            }
        }
        return props;
    }
}
