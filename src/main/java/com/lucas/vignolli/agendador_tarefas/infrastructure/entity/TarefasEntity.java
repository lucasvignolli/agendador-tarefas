package com.lucas.vignolli.agendador_tarefas.infrastructure.entity;


import com.lucas.vignolli.agendador_tarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("tarefas")
public class TarefasEntity {

    @Id
    private String id;
    private String nomeTarefa;
    private String descricao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataEvento;
    private String emailUsuario;
    private LocalDateTime dataAteracao;
    private StatusNotificacaoEnum statusNotificacaoEnum;

}
