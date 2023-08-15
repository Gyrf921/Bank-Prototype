package com.bankprototype.deal.mapper;

import com.bankprototype.deal.repository.dao.jsonb.Employment;
import com.bankprototype.deal.web.dto.EmploymentDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T14:06:49+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
public class EmploymentMapperImpl implements EmploymentMapper {

    @Override
    public EmploymentDTO employmentToEmploymentDto(Employment employment) {
        if ( employment == null ) {
            return null;
        }

        EmploymentDTO.EmploymentDTOBuilder employmentDTO = EmploymentDTO.builder();

        employmentDTO.employmentStatus( employment.getStatus() );
        employmentDTO.employerINN( employment.getEmployerInn() );
        employmentDTO.salary( employment.getSalary() );
        employmentDTO.position( employment.getPosition() );
        employmentDTO.workExperienceTotal( employment.getWorkExperienceTotal() );
        employmentDTO.workExperienceCurrent( employment.getWorkExperienceCurrent() );

        return employmentDTO.build();
    }

    @Override
    public Employment employmentDtoToEmployment(EmploymentDTO employmentDTO) {
        if ( employmentDTO == null ) {
            return null;
        }

        Employment.EmploymentBuilder employment = Employment.builder();

        employment.status( employmentDTO.getEmploymentStatus() );
        employment.employerInn( employmentDTO.getEmployerINN() );
        employment.salary( employmentDTO.getSalary() );
        employment.position( employmentDTO.getPosition() );
        employment.workExperienceTotal( employmentDTO.getWorkExperienceTotal() );
        employment.workExperienceCurrent( employmentDTO.getWorkExperienceCurrent() );

        return employment.build();
    }
}
