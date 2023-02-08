package com.in28minutes.rest.webservices.restfulwebservices.versioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersioningPersonController {

    @GetMapping("/v1/person")
    public PersonV1 getFirstVersionOfPerson() {
        return new PersonV1("Bob Charlie");
    }

    @GetMapping("/v2/person")
    public PersonV2 get2ndVersionOfPerson() {
        return new PersonV2(new Name("Bob ","Charlie"));
    }

    @GetMapping(path="/person",params="version=1")
    public PersonV1 getPersonParamsV1() {
        return getFirstVersionOfPerson();
    }


    @GetMapping(path="/person",params="version=2")
    public PersonV2 getPersonParamsV2() {
        return get2ndVersionOfPerson();
    }

    @GetMapping(path="/person",headers="X-API-VERSION=1")
    public PersonV1 getPersonHeaderV1() {
        return getFirstVersionOfPerson();
    }

    @GetMapping(path="/person",headers="X-API-VERSION=2")
    public PersonV2 getPersonHeaderV2() {
        return get2ndVersionOfPerson();
    }


    @GetMapping(path="/person",produces="application/vnd.company.app-v1+json")
    public PersonV1 getPersonAcceptHeaderV1() {
        return getFirstVersionOfPerson();
    }

    @GetMapping(path="/person",produces="application/vnd.company.app-v2+json")
    public PersonV2 getPersonAcceptHeaderV2() {
        return get2ndVersionOfPerson();
    }

}
