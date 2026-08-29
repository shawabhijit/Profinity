package com.profinity.userservice.event;

import java.util.UUID;

public record EducationEvent (
        UUID id,
        String school,
        String degree,
        String fieldOfStudy
){
}
