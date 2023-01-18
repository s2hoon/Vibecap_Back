
package com.example.vibecap_back.domain.post.domain.Count.Scrap;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class PostScrapCount implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    private Long member_id;

    @EqualsAndHashCode.Include
    @Id
    private Long post_id;
}