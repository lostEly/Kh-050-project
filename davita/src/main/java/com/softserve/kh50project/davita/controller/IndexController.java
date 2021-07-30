package com.softserve.kh50project.davita.controller;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping("/")
public class IndexController {


    static class CustomRepresentationModel extends RepresentationModel<CustomRepresentationModel> {
        public CustomRepresentationModel(Link initialLink) {
            super(initialLink);
        }
    }

/*    @GetMapping
    public CustomRepresentationModel index() {
        return new CustomRepresentationModel(linkTo(EquipmentController.class).withRel("equipment"));
    }*/

    @GetMapping
    public CustomRepresentationModel index() {
        return new CustomRepresentationModel(linkTo(EquipmentController.class).withRel("procedures"));
    }

}
