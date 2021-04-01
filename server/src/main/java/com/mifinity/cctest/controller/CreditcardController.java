package com.mifinity.cctest.controller;


import com.mifinity.cctest.exceptions.BadRequestException;
import com.mifinity.cctest.model.Creditcard;
import com.mifinity.cctest.model.User;
import com.mifinity.cctest.repository.CreditcardRepository;
import com.mifinity.cctest.security.AuthoritiesConstants;
import com.mifinity.cctest.service.UserService;
import com.mifinity.cctest.service.dto.CreditcardDTO;
import com.mifinity.cctest.service.dto.UserDTO;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.authentication.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mifinity.cctest.model.Creditcard}.
 */
@Controller("/api")
public class CreditcardController {

    private final Logger log = LoggerFactory.getLogger(CreditcardController.class);

    @Inject
    private CreditcardRepository creditcardRepository;

    @Inject
    private UserService userService;
    /**
     * {@code POST  /creditcards} : Create a new creditcard.
     *
     * @param creditcard the creditcard to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new creditcard, or with status {@code 400 (Bad Request)} if the creditcard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/creditcards")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Creditcard> createCreditcard(Authentication authentication, @Body Creditcard creditcard) throws URISyntaxException {
        log.info("REST request to save creditcard : {}", creditcard);
        if (creditcard.getId() != null) {
            throw new BadRequestException("A new creditcard cannot already have an ID");
        }
        UserDTO userDTO = new UserDTO(authentication);
        if (userDTO.getRoles().contains(AuthoritiesConstants.USER)) {
            creditcard.setUser(userService.getUserById(userDTO.getId()).orElse(new User()));
        }
        creditcard.setCreatedBy(authentication.getName());
        Creditcard result = creditcardRepository.save(creditcard);
        URI location = new URI("/api/creditcard/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
        });
    }

    /**
     * {@code GET  /creditcards} : get all the creditcards.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of creditcards in body.
     */
    @Get("/creditcards")
    @ExecuteOn(TaskExecutors.IO)
    public Page<Creditcard> getAllCreditcards(Authentication authentication, Pageable pageable) {
        log.debug("REST request to get all creditcards by {}", authentication.getName());
        UserDTO userDTO = new UserDTO(authentication);
        if (userDTO.getRoles().contains(AuthoritiesConstants.USER)) {
            return creditcardRepository.findByUserId(userDTO.getId(), pageable);
        }
        return creditcardRepository.findAll(pageable);
    }


    /**
     * {@code PUT  /creditcards/id} : Updates an existing creditcard.
     *
     * @param creditcardDTO the properties of creditcard to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated creditcard,
     * or with status {@code 400 (Bad Request)} if the creditcard is not valid,
     * or with status {@code 500 (Internal Server Error)} if the creditcard couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/creditcards/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Creditcard> updateCreditcard(Authentication authentication, @PathVariable Long id, @Parameter CreditcardDTO creditcardDTO) throws URISyntaxException {
        log.debug("REST request to update creditcard with id : {} by {}", id, authentication.getName());
        if (id.compareTo(creditcardDTO.getId())!=0) {
            throw new BadRequestException("Creditcard information does not match");
        }
        UserDTO userDTO = new UserDTO(authentication);
        Creditcard creditcard = creditcardRepository.findById(id).orElseThrow(() -> new BadRequestException("No credit card found"));
        if (userDTO.getRoles().contains(AuthoritiesConstants.USER) &&
          !userDTO.getId().equals(creditcard.getUser().getId())) {
            log.error("Update creditcard failed as the creditcard: {} does not belong to user: {}", id, userDTO.getId());
            throw new BadRequestException("Creditcard is not authorised to be updated by the user");
        }
        creditcard.setExpiryDate(creditcardDTO.getExpiryDate());
        creditcard.setUpdatedBy(authentication.getName());
        creditcard.setUpdatedDate(ZonedDateTime.now(ZoneId.systemDefault()).toInstant());
        Creditcard result = creditcardRepository.update(creditcard);
        return HttpResponse.ok(result);
    }

    /**
     * {@code GET  /creditcards/:id} : get the creditcard by "id".
     *
     * @param id the id of the creditcard to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the creditcard, or with status {@code 404 (Not Found)}.
     */
    @Get("/creditcards/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<Creditcard> getCreditcard(Authentication authentication, @PathVariable Long id) {
        log.debug("REST request to get creditcard : {} by {}", id, authentication.getName());
        UserDTO userDTO = new UserDTO(authentication);
        if (userDTO.getRoles().contains(AuthoritiesConstants.USER)) {
            return creditcardRepository.findByIdAndUserId(id,userDTO.getId());
        }
        return creditcardRepository.findById(id);
    }

    /**
     * {@code GET  /creditcards/number/:number} : get the creditcard by "number".
     *
     * @param number the id of the creditcard to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with list of creditcards, or with status {@code 404 (Not Found)}.
     */
    @Get("/creditcards/number/{number}")
    @ExecuteOn(TaskExecutors.IO)
    public Page<Creditcard> getCreditcard(Authentication authentication, @PathVariable String number, Pageable pageable) {
        log.info("REST request to get creditcard matching number: {} by {}", number, authentication.getName());
        UserDTO userDTO = new UserDTO(authentication);
        StringBuilder builder = new StringBuilder("%").append(number).append("%");
        if (userDTO.getRoles().contains(AuthoritiesConstants.USER)) {
            return creditcardRepository.findByUserAndNumberLike(userDTO.getId(), builder.toString(), pageable);
        }
        return creditcardRepository.findByNumberLike(builder.toString(), pageable);
    }

    /**
     * {@code DELETE  /creditcards/:id} : delete the creditcard with "id".
     *
     * @param id the id of the creditcard to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/creditcard/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteCreditcard(Authentication authentication, @PathVariable Long id) {
        log.debug("REST request to delete creditcard : {}", id);
        UserDTO userDTO = new UserDTO(authentication);
        if (userDTO.getRoles().contains(AuthoritiesConstants.USER)) {
            creditcardRepository.deleteById(id, userDTO.getId());
        } else {
            creditcardRepository.deleteById(id);
        }
        return HttpResponse.noContent();
    }
}
