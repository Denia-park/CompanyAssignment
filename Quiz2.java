/*
[2번 문제]
* 서로 다른 종류의 통화를 나타내는 N 크기의 coin[ ] 정수 배열과 정수 합계가 주어지면, coin[]의 다양한 조합을 사용하여 합계를 만드는 방법의 수를 찾는 것이 과제입니다.

참고: 각 유형의 동전이 무한정 공급된다고 가정합니다.

입력: sum = 4, coins[] = {1,2,3},
출력: 4
설명: {1, 1, 1, 1}, {1, 1, 2}, {2, 2}, {1, 3}의 네 가지 솔루션이 있습니다.


결과 예시)
입력: 합계 = 10, coins[] = {2, 5, 3, 6}
출력: 5
설명: 다섯 가지 솔루션이 있습니다.
{2,2,2,2,2}, {2,2,3,3}, {2,2,6}, {2,3,5} 및 {5,5}.
 */

public class Quiz2 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        //출력 : 4, {1, 1, 1, 1}, {1, 1, 2}, {2, 2}, {1, 3}
        int exampleAnswer1 = solution.solve(4, new int[]{1, 2, 3});
        System.out.println(exampleAnswer1);

        //출력 : 5, {2,2,2,2,2}, {2,2,3,3}, {2,2,6}, {2,3,5}, {5,5}
        int exampleAnswer2 = solution.solve(10, new int[]{2, 5, 3, 6});
        System.out.println(exampleAnswer2);
    }
}

class Solution {
    public int solve(int sum, int[] coins) {
        //dp를 사용하여 풀이를 진행 (이전에 사용했던 값을 그대로 사용할 수 있기 때문에 효율적이다.)
        int[] dp = new int[sum + 1];

        //계산을 편리하게 하기 위해서 사용
        //이렇게 미리 넣어둘 경우  -> 예를 들어, 2원 코인으로 2원을 만들때 점화식만으로 1이라는 값이 나온다.
        dp[0] = 1;

        for (int coin : coins) {
            //coin보다 작은 금액은 만들지 못하므로 value는 coin부터 시작한다.
            for (int value = coin; value <= sum; value++) {
                //preCoinCount가 존재하면, 이번 coin을 사용해서 preCoinCount개 만큼 value 값을 만들 수 있다는 의미
                int preCoinCount = dp[value - coin];

                //기존의 dp[value]를 더하는 이유는 이전의 코인들을 사용해서 만들 수 있는 최대 값이 이미 저장되어 있기 때문에
                dp[value] += preCoinCount;
            }
        }

        return dp[sum];
    }
}
