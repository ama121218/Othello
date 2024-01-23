
import java.util.*;


public class Main{

    private String[][] gameBoard;
    private final int[] dx_array = {-1,-1,-1,0,0,1,1,1};
    private final int[] dy_array = {-1,0,1,-1,1,-1,0,1};
    private final String[] playerColor = {"B","W"};

    private int N;
    private int M;

    public static void main(String[] args){

        Main main = new Main();
        Scanner sc = new Scanner(System.in);
        main.initializeGame(sc);
        main.printGameBoard();
        main.startGame(sc);
    }

    public void initializeGame(Scanner sc){
        while(true) {
            try {
                System.out.println("盤面の大きさを入力してください x: y: (x > 4 && y > 4)");
                N = sc.nextInt();
                M = sc.nextInt();
                break;
            } catch (NumberFormatException e) {
                System.out.println("数字以外が入力されました。");
            }
        }
        gameBoard = new String[N][M];
        for(int i=0;i<N;i++)for(int j=0;j<M;j++)gameBoard[i][j] = ".";
        gameBoard[3][3] = "W";
        gameBoard[3][4] = "B";
        gameBoard[4][3] = "B";
        gameBoard[4][4] = "W";
    }

    public void startGame(Scanner sc){
        int countPlayer = 0;
        while(isComplete()){
            int tempCountPlayer = allCheck(countPlayer);
            if(tempCountPlayer==-1){
                System.out.println("おける駒がなくなりました");
                break;
            }else if(tempCountPlayer!=countPlayer){
                System.out.println("おける駒がないためパスになります");
                countPlayer = tempCountPlayer;
            }
            if(countPlayer == 0)System.out.println("座標を入力してください(プレイヤー:黒) x: y: (0 < x < "+N+" 0 <  y < "+M+")");
            if(countPlayer == 1)System.out.println("座標を入力してください(プレイヤー:白) x: y: (0 < x < "+N+" 0 <  y < "+M+")");
            String command = sc.next();
            if(command.equals("quit")){
                return;
            }else if(command.equals("restart")){
                this.initializeGame(sc);
            }else{
                try {
                    int x = Integer.parseInt(command) - 1;
                    int y = Integer.parseInt(sc.next()) - 1;
                    if(x >= 0 && x < N && y >= 0 && y < N && putPiece(x,y,playerColor[countPlayer])){
                        countPlayer = (countPlayer + 1)% 2;
                    }else{
                        System.out.println("その場所にはおけません");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("数字以外が入力されました。");
                }
            }
            printGameBoard();
        }

        int countWhite = countColor("W");
        int countBlack = countColor("B");

        System.out.println("白の数:"+countWhite+" "+"黒の数:"+countBlack);
        if(countWhite>countBlack)System.out.println("白の勝ち");
        else if(countWhite<countBlack)System.out.println("黒の勝ち");
        else System.out.println("引き分け");
    }

    public void printGameBoard(){
        StringBuilder sb = new StringBuilder();
        for(int i=-1;i<N;i++){
            for(int j=0;j<N;j++) {
                if(j==0)sb.append(i!=-1?i+1:" ").append(" ");
                if(i==-1)sb.append(j+1);
                else if(gameBoard[i][j].equals("W")){
                    sb.append("□");
                }else if(gameBoard[i][j].equals("B")){
                    sb.append("■");
                }else{
                    sb.append("-");
                }
                if(j!=M-1)sb.append(" ");
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }


    public int countColor(String target){
        int count = 0;
        for(int i=0;i<N;i++)for(int j=0;j<M;j++){
            if(gameBoard[i][j].equals(target))count++;
        }
        return count;
    }

    public boolean putPiece(int x,int y,String target){
        if(!gameBoard[x][y].equals(".")){
            return false;
        }
        boolean isTurn = turnPiece(x,y,target);
        if(isTurn)gameBoard[x][y] = target;
        return isTurn;
    }

    public boolean turnPiece(int x,int y,String target){
        boolean isTurnPiece = false;
        for(int d = 0;d < dx_array.length;d++) {
            int dx = dx_array[d];
            int dy = dy_array[d];
            int xx = x;
            int yy = y;

            int count = 0;
            boolean isTurn = false;

            Queue<Integer> q_x = new ArrayDeque<>();
            Queue<Integer> q_y = new ArrayDeque<>();

            while (true) {
                xx = xx + dx;
                yy = yy + dy;

                if (xx < 0 || xx > N - 1 || yy < 0 || yy > M - 1) break;
                if (gameBoard[xx][yy].equals(".")) break;
                else if (gameBoard[xx][yy].equals(target) && count >= 1) {
                    isTurn = true;
                    isTurnPiece = true;
                    break;
                }else if(!gameBoard[xx][yy].equals(target)){
                    count++;
                    q_x.add(xx);
                    q_y.add(yy);
                }
            }
            if(isTurn){
                while(q_x.peek()!=null)gameBoard[q_x.poll()][q_y.poll()] = target;
            }

        }
        return isTurnPiece;
    }

    public boolean isTurnPiece(int x,int y,String target){
        boolean isTurn = false;
        for(int d = 0;d < dx_array.length;d++){
            int dx = dx_array[d];
            int dy = dy_array[d];
            int xx = x;
            int yy = y;
            int count = 0;
            while (true) {
                xx = xx + dx;
                yy = yy + dy;

                if (xx < 0 || xx > N - 1 || yy < 0 || yy > M - 1) break;
                if (gameBoard[xx][yy].equals(".")) break;
                else if (gameBoard[xx][yy].equals(target) && count >= 1) {
                    isTurn = true;
                    break;
                }else if(!gameBoard[xx][yy].equals(target)){
                    count ++;
                }
            }
        }
        return isTurn;
    }


    public boolean isComplete(){
        int count = 0;
        for(int i=0;i<N;i++)for(int j=0;j<M;j++){
            if(gameBoard[i][j].equals("."))count++;
        }
        return count>=1;
    }

    public int allCheck(int countPlayer){
        String targetReverseColor = countPlayer==0?"W":"B";

        boolean isReverseColor2 = false;

        for(int i=0;i<N;i++)for(int j=0;j<M;j++){
            if(gameBoard[i][j].equals(targetReverseColor)){
                for(int d = 0;d < dx_array.length;d++) {
                    int xx = i + dx_array[d];
                    int yy = j + dy_array[d];
                    if (xx < 0 || xx > N - 1 || yy < 0 || yy > M - 1) continue;
                    if (gameBoard[xx][yy].equals(".")){
                        if(isTurnPiece(xx,yy,playerColor[countPlayer]))return countPlayer;
                    }
                }
            }else if(gameBoard[i][j].equals(playerColor[countPlayer])){
                for(int d = 0;d < dx_array.length;d++) {
                    int xx = i + dx_array[d];
                    int yy = j + dy_array[d];
                    if (xx < 0 || xx > N - 1 || yy < 0 || yy > M - 1) continue;
                    if (gameBoard[xx][yy].equals(".")){
                        if(isTurnPiece(xx,yy,targetReverseColor))isReverseColor2=true;
                    }
                }
            }
        }
        if(isReverseColor2)return (countPlayer+1)%2;
        else return -1;
    }
}