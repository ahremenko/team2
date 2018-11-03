package dev.java.controller;

import dev.java.Logging;
import dev.java.db.ConnectorDB;
import dev.java.db.daos.AbstractDao;
import dev.java.db.model.Entity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractController<T extends Entity> {
    private final Logging logging = new Logging();
    private final boolean sortType = true;
    protected String sortedField;
    protected String url;
    private int itemsInPage = 3;

    protected AbstractDao<T> abstractDao;
    protected Connection connection;

    @PostConstruct
    public void initialize() {
        try {
            connection = ConnectorDB.getConnection();
            //abstractDao = new CandidateDao(connection);
        } catch (SQLException e) {
            logging.runMe(e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            logging.runMe(e);
        }
    }

    public ResponseEntity getAllEntities(HttpServletRequest request) {
        logging.runMe(request);
        List<T> allEntities;
        try {
            allEntities = abstractDao.getSortedEntitiesPage(1, sortedField, true, itemsInPage);
            System.out.println(allEntities);
            return ResponseEntity.ok(allEntities);
        } catch (SQLException e) {
            return getResponseEntityOnServerError(e);
        }
    }

    public ResponseEntity createEntity(@RequestBody T entity, HttpServletRequest request) {
        logging.runMe(request);
        try {
            if (abstractDao.createEntity(entity)) {
                return ResponseEntity.created(new URI(url + entity.getId()))
                        .body("Created");
            }
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Invalid Input");
        } catch (SQLException | URISyntaxException e) {
            return getResponseEntityOnServerError(e);
        }
    }

    public ResponseEntity getEntity(@PathVariable long id, HttpServletRequest request) {
        logging.runMe(request);
        try {
            T entity = abstractDao.getEntityById(id);
            if (entity == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(entity);
        } catch (SQLException e) {
            return getResponseEntityOnServerError(e);
        }
    }

    public ResponseEntity updateEntity(@PathVariable long id, @RequestBody T entity,
                                       HttpServletRequest request) {
        logging.runMe(request);
        try {
            if (abstractDao.updateEntity(entity)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return getResponseEntityOnServerError(e);
        }
    }

    public ResponseEntity deleteEntity(@PathVariable long id, HttpServletRequest request) {
        logging.runMe(request);
        try {
            if (abstractDao.deleteEntity(abstractDao.getEntityById(id))) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return getResponseEntityOnServerError(e);
        }
    }

    private ResponseEntity getResponseEntityOnServerError(Exception e) {
        logging.runMe(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
    }
}
