package ural.ba.project.UralBA.service;

import org.springframework.stereotype.Service;
import ural.ba.project.UralBA.exepction.ResourceNotFoundException;
import ural.ba.project.UralBA.model.ReasonRefusal;
import ural.ba.project.UralBA.model.Role;
import ural.ba.project.UralBA.model.User;
import ural.ba.project.UralBA.repository.ReasonRefusalRepository;

import java.util.List;

/**
 * Предоставляет методы для отображения причин отказа пользователей вступить в сообщество
 *
 * @author Daria
 */
@Service
public class ReasonRefusalService {

    private final ReasonRefusalRepository reasonRefusalRepository;

    public ReasonRefusalService(ReasonRefusalRepository reasonRefusalRepository) {
        this.reasonRefusalRepository = reasonRefusalRepository;
    }

    /**
     * Отдаёт список причин
     */
    public List<ReasonRefusal> findByAll() {
        return reasonRefusalRepository.findAll();
    }

    /**
     * Сохраняет причину отказа в системе
     */
    public ReasonRefusal save(ReasonRefusal reasonRefusal) {
        return reasonRefusalRepository.save(reasonRefusal);
    }

    /**
     * Удаляет причину отказа в системе
     */
    public void delete(ReasonRefusal reasonRefusal) {
        reasonRefusalRepository.delete(reasonRefusal);
    }

    /**
     * Сохраняет причину отказа в системе, если роль у пользователя "отклонненная заявка"
     * Удаляет причину отказа в системе, если роль меняется на другую
     */
    public void saveAndDeleteCheck(ReasonRefusal reasonRefusal, Role newRole, User user) {
        if (newRole.equals(Role.REJECTED_BID) && !user.getRole().equals(Role.REJECTED_BID)) {
            save(reasonRefusal);
        } else if (user.getRole().equals(Role.REJECTED_BID) && !newRole.equals(Role.REJECTED_BID)) {
            ReasonRefusal reasonRefusalOld = reasonRefusalRepository.findByUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException("Причина отказа у пользователя", "id=" + user.getIdUser()));
            delete(reasonRefusalOld);
        }
    }
}
