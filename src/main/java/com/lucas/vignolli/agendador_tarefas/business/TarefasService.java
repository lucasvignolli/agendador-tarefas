package com.lucas.vignolli.agendador_tarefas.business;


import com.lucas.vignolli.agendador_tarefas.business.dto.TarefasDTO;
import com.lucas.vignolli.agendador_tarefas.business.mapper.TarefaConverter;
import com.lucas.vignolli.agendador_tarefas.infrastructure.entity.TarefasEntity;
import com.lucas.vignolli.agendador_tarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.lucas.vignolli.agendador_tarefas.infrastructure.repository.TarefasRepository;
import com.lucas.vignolli.agendador_tarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefaConverter tarefaConverter;
    private final JwtUtil jwtUtil;

    //Extrai o email do Jwt Token retirando 7 caracteres (Bearer).
    //Seta a data de criaçao da tarefa com hora e data local.
    //Seta o status da notificação como pendente.
    //Informa o email extraido do token.
    //Converte e salva na db.
    public TarefasDTO gravarTarefa(String token, TarefasDTO dto){
        String email = jwtUtil.extractUsername(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);
        return tarefaConverter.paraTarefaDto(tarefasRepository.save(entity));
    }
}
