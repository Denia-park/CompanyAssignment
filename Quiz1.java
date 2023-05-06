/*
[1번 문제]
* 게시판 기능을 개발하던중 추가 요구사항이 발생하였습니다. 이 요구사항을 해결하기 위한 자료구조를 구현하여 제출 바랍니다.
* 구현 언어는 Java 입니다.
* 테스트는 자유롭게 하시기 바랍니다.

요구사항 : 게시판은 여러 게시글을 포함하는 객체이자 데이터 이다. 이 게시판이 여러 형태로 카테고리 구분이 되었으면 한다.
카테고리 구분의 예시는 다음과 같다.

https://drive.google.com/file/d/1yLXVttdX3OnBb0mPoL5J5VNVt2wPXXVm/view?usp=sharing

공지사항은 이름은 같지만 각각 다른 게시판이며
익명 게시판은 모두 같은 게시판이 각각 다른 카테고리에 소속되어 있다.

카테고리들간의 관계 데이터를 parent_idx, child_id 2개의 값으로만 저장 해야 하며 저장된 값을 임의의 자료구조를 통해 구조화 시켜서 위와 같은 관계 데이터를 얻을 수 있어야 한다.

자료구조는 다음과 같은 기능이 필수로 지원 되야 한다.

1. 카테고리 식별자 및 카테고리명으로 검색이 되어야하며, 검색된 결과 값은 해당 카테고리의 하위 카테고리를 모두 담고 있어야 한다.
2. 자료구조는 Json text 로 응답 될 수 있어야 한다. (Json 구조로 변환이 가능해야 한다)

* 핵심은 요구사항을 만족하는 자료구조의 구현입니다.
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Quiz1 {
    static String[] dataSet = new String[]{
            "공지사항 1",
            "첸 2",
            "백현 3",
            "시우민 4",
            "공지사항 5",
            "익명게시판 6",
            "뷔 7",
            "공지사항 8",
            "로제 9",
            "엑소 10",
            "방탄소년단 11",
            "블랙핑크 12",
            "남자 13",
            "여자 14"
    };

    public static void main(String[] args) {
        Map<String, Integer> nameToIdxMap = new HashMap<>();
        Map<Integer, String> idxToNameMap = new HashMap<>();

        dataConverting(nameToIdxMap, idxToNameMap);

        DataStructure ds = new DataStructure(nameToIdxMap, idxToNameMap);
        ds.add(13, 10); //남자 엑소
        ds.add(13, 11); //남자 방탄
        ds.add(14, 12); //여자 블핑

        ds.add(10, 1); //엑소 공지
        ds.add(10, 2); //엑소 첸
        ds.add(10, 3); //엑소 백현
        ds.add(10, 4); //엑소 시우민

        ds.add(11, 5); //방탄 공지
        ds.add(11, 6); //방탄 익명
        ds.add(11, 7); //방탄 뷔

        ds.add(12, 8); //블핑 공지
        ds.add(12, 6); //블핑 익명
        ds.add(12, 9); //블핑 로제

        System.out.println(ds.toJSON());
        System.out.println(ds.search("엑소"));
        System.out.println(ds.search("블랙핑크"));
        System.out.println(ds.search("남자"));
        System.out.println(ds.search(13));
        System.out.println(ds.search("여자"));
        System.out.println(ds.search(14));
    }

    private static void dataConverting(Map<String, Integer> nameToIdxMap, Map<Integer, String> idxToNameMap) {
        for (String str : dataSet) {
            String[] strSplits = str.split(" ");
            String name = strSplits[0];
            int idx = Integer.parseInt(strSplits[1]);

            nameToIdxMap.put(name, idx);
            idxToNameMap.put(idx, name);
        }
    }
}

class DataStructure {
    private final Map<String, Integer> nameToIdxMap;
    private final Map<Integer, String> idxToNameMap;
    private final Category root;

    public DataStructure(Map<String, Integer> nameToIdxMap, Map<Integer, String> idxToNameMap) {
        this.nameToIdxMap = nameToIdxMap;
        this.idxToNameMap = idxToNameMap;
        this.root = new Category(0, "root");
    }

    //자료구조에 데이터 추가
    //parent_idx, child_id 2개의 값으로만 저장
    public void add(int parentIdx, int childIdx) {
        Category parent = root.search(parentIdx);
        Category child = new Category(childIdx, idxToNameMap.get(childIdx));

        if (parent != null) {
            parent.add(child);
            return;
        }

        parent = new Category(parentIdx, idxToNameMap.get(parentIdx));

        parent.add(child);
        root.add(parent);
    }

    //카테고리명으로 검색
    public Category search(String name) {
        //Map에서 name 기반으로 Idx 찾기
        int searchIdx = nameToIdxMap.get(name);

        //Idx 기반으로 카테고리 검색
        return root.search(searchIdx);
    }

    //카테고리 식별자로 검색
    public Category search(int idx) {
        //idx 기반으로 카테고리 검색
        return root.search(idx);
    }

    //JSON 화
    public String toJSON() {
        return root.toJSON().get("childs").toString();
    }
}

class Category {
    private final int idx;
    private final String name;
    private final List<Category> childs;

    public Category(int idx, String name) {
        this.idx = idx;
        this.name = name;
        this.childs = new LinkedList<>();
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public List<Category> getChilds() {
        return childs;
    }

    public void add(Category child) {
        this.childs.add(child);
    }

    public Category search(int idx) {
        //Data가 비어있으므로 null 반환
        if (childs.isEmpty()) {
            return null;
        }

        for (Category child : childs) {
            if (child.idx == idx) {
                return child;
            }

            Category childResult = child.search(idx);
            if (childResult == null) {
                continue;
            }

            return childResult;
        }

        return null;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idx", this.idx);
        jsonObject.put("name", this.name);

        if (childs.isEmpty()) {
            return jsonObject;
        }

        JSONArray childJsonArray = new JSONArray();
        for (Category child : childs) {
            childJsonArray.add(child.toJSON());
        }

        jsonObject.put("childs", childJsonArray);
        return jsonObject;
    }
}
