package com.example.HiBuddy.domain.script;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScriptsService {
    public List<Scripts> getAllScripts() {
        List<Scripts> scripts = new ArrayList<>();

        Scripts script1 = new Scripts();
        script1.setId(1L);
        script1.setScriptName("Script 1");
        script1.setDifficulty(Difficulty.EASY);
        script1.setText("안녕하세요, 저는 현재 인하대학교 학생입니다. 인하대학교는 인천에 있습니다. 저는 코딩을 좋아합니다. 요즘 날씨가 너무 더워요.");

        Scripts script2 = new Scripts();
        script2.setId(2L);
        script2.setScriptName("Script 2");
        script2.setDifficulty(Difficulty.MEDIUM);
        script2.setText("저는 지난 주말에 친구들과 함께 서울을 여행했습니다. 우리는 남산 타워에 올라가서 서울 시내를 구경했어요. 정말 멋졌어요. 여행 중에 전통 시장도 방문해서 다양한 한국 음식을 맛볼 수 있었어요.");

        Scripts script3 = new Scripts();
        script3.setId(3L);
        script3.setScriptName("Script 3");
        script3.setDifficulty(Difficulty.HARD);
        script3.setText("나라의 말이 중국과 달라 문자와 서로 통하지 아니하므로, 이런 가닥으로 어리석은 백성이 이르고자 하는 바가 있어도 마침내 제 뜻을 능히 펴지 못하는 사람이 많으니라, 내가 이를 위하여 가볍게 여겨 새로 스물열 자를 만드니 사람마다 하여금 쉽게 익혀 날로 쓰기 편하게 하고자 할 따름이니라.");

        scripts.add(script1);
        scripts.add(script2);
        scripts.add(script3);

        return scripts;
    }

    public Scripts getScriptById(Long scriptId) {
        return getAllScripts().stream()
                .filter(script -> script.getId().equals(scriptId))
                .findFirst()
                .orElse(null);
    }
}
