package com.in28minutes.rest.webservices.restfulwebservices.filtering;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class FilteringController {

    @GetMapping("/filtering")
    public MappingJacksonValue filtering() {
        SomeBean someBeam=new SomeBean("value1", "value2", "value4");

        MappingJacksonValue mappingJasonValue = filterMappingJacksonValue(someBeam,"field1","field3");

        return mappingJasonValue;
    }



    @GetMapping("/filtering-list")
    public MappingJacksonValue filteringList() {

        List<SomeBean> list = Arrays.asList(new SomeBean("value1", "value2", "value4"),
                                            new SomeBean("value4", "value5", "value6"));

        MappingJacksonValue mappingJasonValue = filterMappingJacksonValue(list,"field2","field3");

        return mappingJasonValue;
    }

    private static MappingJacksonValue filterMappingJacksonValue(Object object,String ...includedFieldName) {
        MappingJacksonValue mappingJasonValue=new MappingJacksonValue(object);

        SimpleBeanPropertyFilter filter=SimpleBeanPropertyFilter.filterOutAllExcept(includedFieldName);
        FilterProvider filters= new SimpleFilterProvider().addFilter("SomeBeanFilter",filter);
        mappingJasonValue.setFilters(filters);
        return mappingJasonValue;
    }

}