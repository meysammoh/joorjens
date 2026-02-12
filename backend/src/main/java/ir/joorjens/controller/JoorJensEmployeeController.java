package ir.joorjens.controller;

import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.JoorJensEmployeeRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.model.entity.JoorJensEmployee;
import ir.joorjens.model.util.FormFactory;

import java.util.Optional;

public class JoorJensEmployeeController {

    private static final JoorJensEmployeeRepository REPO_JOOR_EMP = (JoorJensEmployeeRepository) RepositoryManager.getByEntity(JoorJensEmployee.class);
    private static final FormFactory<JoorJensEmployee> FORM = new FormFactory<>(JoorJensEmployee.class);

    public JoorJensEmployeeController() {

    }

    //-------------------------------------------------------------------------------------------------
    static JoorJensEmployee getJoorJensEmployee(final long id) throws JoorJensException {
        final Optional<JoorJensEmployee> joorJensEOptional = REPO_JOOR_EMP.getByKey(id);
        if (!joorJensEOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, JoorJensEmployee.getEN());
        }
        return joorJensEOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}