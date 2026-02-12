package com.lucas.vignolli.agendador_tarefas.business;


import com.lucas.vignolli.agendador_tarefas.business.dto.TarefasDTO;
import com.lucas.vignolli.agendador_tarefas.business.mapper.TarefaConverter;
import com.lucas.vignolli.agendador_tarefas.business.mapper.TarefaUpdateConverter;
import com.lucas.vignolli.agendador_tarefas.infrastructure.entity.TarefasEntity;
import com.lucas.vignolli.agendador_tarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.lucas.vignolli.agendador_tarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.lucas.vignolli.agendador_tarefas.infrastructure.repository.TarefasRepository;
import com.lucas.vignolli.agendador_tarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefaConverter tarefaConverter;
    private final JwtUtil jwtUtil;
    private final TarefaUpdateConverter tarefaUpdateConverter;

    //Extrai o email do Jwt Token retirando 7 caracteres (Bearer).
    //Seta a data de criaçao da tarefa com hora e data local.
    //Seta o status da notificação como pendente.
    //Informa o email extraido do token.
    //Converte e salva na db.
    public TarefasDTO gravarTarefa(String token, TarefasDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);
        return tarefaConverter.paraTarefaDto(tarefasRepository.save(entity));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefaConverter.paraListaTarefasDto(tarefasRepository.findByDataEventoBetweenAndStatusNotificacaoEnum(dataInicial, dataFinal, StatusNotificacaoEnum.PENDENTE));
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        return tarefaConverter.paraListaTarefasDto(tarefasRepository.findByemailUsuario(email));
    }

    public void deletaTarefaPorId(String id) {
        try {
            tarefasRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao deletar tarefa por Id, Id não encontrado " + id + e.getCause());
        }
    }

    public TarefasDTO alteraStatus(String id, StatusNotificacaoEnum status) {
        try {
            TarefasEntity entity = tarefasRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada " + id));
            entity.setStatusNotificacaoEnum(status);
            return tarefaConverter.paraTarefaDto(tarefasRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException("Erro ao encontrar tarefa " + e.getCause());
        }
    }

    public TarefasDTO alteraTarefa(TarefasDTO dto, String id){
        try {
            TarefasEntity entity = tarefasRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada " + id));
            tarefaUpdateConverter.updeateDeTarefa(dto, entity);
            return tarefaConverter.paraTarefaDto(tarefasRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException("Erro ao encontrar tarefa " + e.getCause());
    }

    }
}