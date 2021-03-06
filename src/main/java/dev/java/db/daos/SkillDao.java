package dev.java.db.daos;

import dev.java.db.model.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillDao extends AbstractDao<Skill> {

    private static String SQL_SELECT_BY_NAME = "SELECT * FROM skill AS s WHERE s.name=?";
    //language=SQL
    private static String SQL_DELETE = "DELETE FROM skill WHERE name=?";

    public SkillDao(Connection connection) {
        super(connection);
        SQL_SELECT_SORTED_PAGE = "SELECT * FROM skill ORDER BY %s %s LIMIT ?, ?";
        SQL_INSERT = "INSERT INTO skill "
                + "(name) "
                + "VALUES (?)";
        SQL_UPDATE = "UPDATE skill "
                + "SET name=? "
                + "WHERE name=?";
        SQL_SELECT_FILTERED_ENTITIES = "SELECT * FROM skill "
                + "WHERE  (name=? OR ?='')";
    }

    @Override
    public final boolean createEntity(Skill entity) throws SQLException {
        try (PreparedStatement insertPrepareStatement = connection.prepareStatement(SQL_INSERT)) {
            setValuesForInsertIntoPrepareStatement(insertPrepareStatement, entity);
            int status =  insertPrepareStatement.executeUpdate();
            return status > 0;
        }
    }

    @Override
    public final boolean updateEntity(Skill entity) {
        throw new UnsupportedOperationException();
    }

    public final boolean deleteEntity(Skill entity) throws SQLException {
        try (PreparedStatement insertPrepareStatement = connection.prepareStatement(SQL_DELETE)) {
            setValuesForInsertIntoPrepareStatement(insertPrepareStatement, entity);
            int status =  insertPrepareStatement.executeUpdate();
            return status > 0;
        }
    }

    public final Skill getEntityByName(String name) throws SQLException {
        try (PreparedStatement getByIdPrepareStatement = connection.prepareStatement(SQL_SELECT_BY_NAME)) {
            getByIdPrepareStatement.setString(1, name);
            ResultSet entity = getByIdPrepareStatement.executeQuery();
            if (entity.next()) {
                Skill skill = setEntityFields(entity);
                entity.close();
                return skill;
            }
            return null;
        }
    }

    @Override
    protected final void setValuesForInsertIntoPrepareStatement(PreparedStatement prepareStatement, Skill skill)
            throws SQLException {
        prepareStatement.setString(1, skill.getName());
    }

    @Override
    protected final void setValuesForUpdateIntoPrepareStatement
            (PreparedStatement prepareStatement, Skill skill) throws SQLException {
        setValuesForInsertIntoPrepareStatement(prepareStatement, skill);
        prepareStatement.setString(2, skill.getName());

    }

    @Override
    protected final Skill setEntityFields(ResultSet candidateTableRow) throws SQLException {
        Skill skill = new Skill();
        skill.setName(candidateTableRow.getString("name"));
        return skill;
    }
}
