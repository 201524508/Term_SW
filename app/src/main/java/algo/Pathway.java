package algo;

/**
 * Created by YUUUUU on 2017-06-05.
 */

public class Pathway {
    private Spot list[];
    private Spot path[];
    private int numSpot;
    /*
    private int totCostCar;
    private int totCostTaxi;
    private int totTime;
    */
    private String pathway;

    public Pathway() {
        list = null;
        path = null;
        numSpot = 0;
        /*
        totCostCar = 0;
        totCostTaxi = 0;
        totTime = 0;
        */
        pathway = "";
    }

    public Pathway(int n){
        list = new Spot[n];
        for(int i = 0; i < n; i++){
            list[i] = new Spot(i);
            path[i] = new Spot();
        }
        path = new Spot[n];
        numSpot = n;
        /*
        totCostCar = 0;
        totCostTaxi = 0;
        totTime = 0;
        */
        pathway = "";
    }

   public void insertSpot(Spot p, Spot q){
        if(p == null){
            p = q;
        }
        else{
            while(p.getNext() != null){
                p = p.getNext();
            }
            p.setNext(q);
        }
    }

    public void insertSpot(Spot p){
        int l = p.getLabel();
        insertSpot(list[l], p);
    }

    /* 자동차 최소비용 */
    public int cheapestCarCost(){
        int result = 0;
        int lmp1 = 0;   //기존 label
        int tmp1 = 0;   //기존 costCar
        int cnt = 0;
        int pathLabel[] = new int[numSpot];

        myStack visit = new myStack(numSpot);

        for(int i = 0; i < numSpot; i++){
            pathLabel[i] = -1;
        }

        pathLabel[0] = 0;
        visit.push(0);
        cnt = cnt + 1;

        for(int i = 0; i < numSpot; i++){
            if(list[lmp1] == null){
                return -1;
            }

            /* 리스트 내 tmp, lmp */
            int tmp2;
            int lmp2;
            /* 리스트 외 배열에 대한 tmp, lmp */
            int tmp3;
            int lmp3;

            Spot t = new Spot();
            if(list[lmp1].getNext() != null){   //Spot t 설정
                t = list[lmp1].getNext();
            }
            else{
                if(lmp1 == numSpot - 1){    //마지막 순서면 break
                    if(cnt == numSpot - 1){
                        pathLabel[numSpot - 1] = numSpot - 1;
                        result = result + tmp1;
                        break;
                    }
                    else{
                        return -1;
                    }
                }
                else{
                    return -1;
                }
            }

            /* tmp2, lmp2 초기화 */
            tmp2 = t.getCostCar(); //getNext().getCost()
            lmp2 = t.getLabel();    //getNext().getLabel()

            tmp3 = tmp2;
            lmp3 = lmp2;

            /* cnt가 numSpot 이 아닐때 -> numSpot - 1 레이블은 확인 안 함 */
            if(cnt != numSpot - 1){
                /* 리스트 내 tmp2, lmp2 설정 */
                if(t.getLabel() != numSpot - 1){
                    while(t.getNext() != null){
                        if(t.getNext().getLabel() != numSpot - 1){
                            if(visit.isIn(t.getNext().getLabel()) != true){
                                if(tmp2 > t.getNext().getCostCar()){
                                    tmp2 = t.getNext().getCostCar();
                                    lmp2 = t.getNext().getLabel();
                                }
                            }
                        }
                        t = t.getNext();
                    }
                    t = list[lmp1].getNext();   /* while에서 바뀐 t를 다시 초기 상태로 설정 */
                }

                //리스트 외 배열에 대한 tmp3, lmp3 설정 : 출발지(0)와 바로 다음 경유지(1)에 대해서는 설정 안 함
                if(lmp1 > 1){
                    Spot k = new Spot();

                    if(lmp2 != numSpot - 1){
                        //tmp3, lmp3를 tmp2, lmp2로 초기화 : tmp2와의 비교를 위함
                        tmp3 = tmp2;
                        lmp3 = lmp2;

                        for(int j = 1; j < lmp1 - 1; j++){  //tmp3, lmp3 설정
                            if(visit.isIn(j) != true){  //j번째 Spot이 visit되지 않은 상태라면
                                if(list[j].getNext() != null){  //list[j]의 getNext()가 존재한다면
                                    k = list[j].getNext();

                                    while(k != null){
                                        if(visit.isIn(k.getLabel()) != true){
                                            if(k.getLabel() == lmp1){   //getNext()의 레이블이 lmp1이랑 같으면
                                                if(tmp3 > k.getCostCar()){ //비교해서 tmp3, lmp3 설정
                                                    tmp3 = k.getCostCar();
                                                    lmp3 = j;
                                                }
                                                break;  //while문 나감
                                            }
                                        }
                                        k = k.getNext();
                                    }
                                }
                            }
                        }
                    }
                    else if(lmp2 == numSpot - 1){
                        for(int j = 1; j < lmp1 - 1; j++){
                            if(visit.isIn(j) != true){
                                Spot h = list[j].getNext();

                                while(h != null){
                                    if(h.getLabel() == lmp1){
                                        tmp3 = h.getCostCar();
                                        lmp3 = j;
                                        break;
                                    }
                                    h = h.getNext();
                                }
                            }
                        }

                        for(int j = 1; j < lmp1 - 1; j++){
                            if(visit.isIn(j) != true){
                                if(list[j].getNext() != null){
                                    k = list[j].getNext();

                                    while(k != null){
                                        if(visit.isIn(k.getLabel()) != true){
                                            if(k.getLabel() == lmp1){
                                                if(tmp3 > k.getCostCar()){
                                                    tmp3 = k.getCostCar();
                                                    lmp3 =j;
                                                }
                                            }
                                        }
                                        k = k.getNext();
                                    }
                                }
                            }
                        }
                    }
                }

                //tmp3, lmp3를 설정한 후
                //tmp2, lmp2와 tmp3, lmp3를 비교해서 tmp1, lmp1을 설정
                if(tmp2 <= tmp3){
                    if(lmp2 != numSpot - 1){
                        lmp1 = lmp2;
                        tmp1 = tmp2;
                    }
                    else{
                        lmp1 = lmp2;
                        tmp1 = tmp3;
                    }
                }
                else if(tmp2 > tmp3){
                    lmp1 = lmp3;
                    tmp1 = tmp3;
                }
            }
            //cnt가 numSpot일 때 -> numSopt - 1 레이블에 대해서만 확인
            else if(cnt == numSpot - 1){
                if(t.getLabel() == numSpot - 1){
                    tmp1 = t.getCostCar();
                    lmp1 = numSpot - 1;
                }
                else{
                    while(t.getNext() != null){
                        if(t.getNext().getLabel() == numSpot - 1){
                            tmp1 = t.getNext().getCostCar();
                            lmp1 = numSpot - 1;
                        }
                        t = t.getNext();
                    }
                }
            }

            pathLabel[i + 1] = lmp1;
            result = result + tmp1;
            visit.push(lmp1);
            cnt = cnt + 1;
        }

        for(int i = 0; i < numSpot - 1; i++){
            int l1 = pathLabel[i];
            int l2 = pathLabel[i+1];
            Spot p = list[l1];
            Spot q = new Spot();

            while(p != null){
                if(p.getLabel() == l2){
                    q = p;
                    break;
                }
                p = p.getNext();
            }

            path[i+1] = q;
        }

        //최종 결과
       // totCostCar = result;
        setPathway();
        return result;
    }

    /* 자동차 택시비용 */
    public int totalTaxiCost(){
        int result = 0;

        for(int i = 1; i < numSpot; i++){
            result = result + path[i].getCostTaxi();
        }

        return result;
    }

    /* 자동차 최소비용 시간 */
    public int totalTime(){
        int result = 0;

        for(int i = 1; i < numSpot; i++){
            result = result + path[i].getTime();
        }

        return result;
    }

    /* 최단거리 */
    public int shortestTime(){
        int result = 0;
        int lmp1 = 0;
        int tmp1 = 0;
        int cnt = 0;
        int pathLabel[] = new int[numSpot];

        myStack visit = new myStack(numSpot);

        for(int i = 0; i < numSpot; i++){
            pathLabel[i] = -1;
        }

        pathLabel[0] = 0;
        visit.push(0);
        cnt = cnt + 1;

        for(int i = 0; i < numSpot; i++){
            if(list[lmp1] == null){
                return -1;
            }

            int tmp2;
            int lmp2;
            int tmp3;
            int lmp3;

            Spot t = new Spot();
            if(list[lmp1].getNext() != null){
                t = list[lmp1].getNext();
            }
            else{
                if(lmp1 == numSpot - 1){
                    if(cnt == numSpot - 1){
                        pathLabel[numSpot - 1] = numSpot - 1;
                        result = result + tmp1;
                        break;
                    }
                    else{
                        return -1;
                    }
                }
                else{
                    return -1;
                }
            }

            tmp2 = t.getTime();
            lmp2 = t.getLabel();

            tmp3 = tmp2;
            lmp3 = lmp2;

            if(cnt != numSpot - 1){
                if(t.getLabel() != numSpot - 1){
                    while(t.getNext() != null){
                        if(t.getNext().getLabel() != numSpot - 1){
                            if(visit.isIn(t.getNext().getLabel()) != true){
                                if(tmp2 > t.getNext().getTime()){
                                    tmp2 = t.getNext().getTime();
                                    lmp2 = t.getNext().getLabel();
                                }
                            }
                        }
                        t = t.getNext();
                    }
                    t = list[lmp1].getNext();
                }

                if(lmp1 > 1){
                    Spot k = new Spot();

                    if(lmp2 != numSpot - 1){
                        tmp3 = tmp2;
                        lmp3 = lmp2;

                        for(int j = 1; j < lmp1 - 1; j++){
                            if(visit.isIn(j) != true){
                                if(list[j].getNext() != null){
                                    k = list[j].getNext();

                                    while(k != null){
                                        if(visit.isIn(k.getLabel()) != true){
                                            if(k.getLabel() == lmp1){
                                                if(tmp3 > k.getTime()){
                                                    tmp3 = k.getTime();
                                                    lmp3 = j;
                                                }
                                                break;
                                            }
                                        }
                                        k = k.getNext();
                                    }
                                }
                            }
                        }
                    }
                    else if(lmp2 == numSpot - 1){
                        for(int j = 1; j < lmp1 - 1; j++){
                            if(visit.isIn(j) != true){
                                Spot h = list[j].getNext();

                                while(h != null){
                                    if(h.getLabel() == lmp1){
                                        tmp3 = h.getTime();
                                        lmp3 = j;
                                        break;
                                    }
                                    h = h.getNext();
                                }
                            }
                        }

                        for(int j = 1; j < lmp1 - 1; j++){
                            if(visit.isIn(j) != true){
                                if(list[j].getNext() != null){
                                    k = list[j].getNext();

                                    while(k != null){
                                        if(visit.isIn(k.getLabel()) != true){
                                            if(k.getLabel() == lmp1){
                                                if(tmp3 > k.getTime()){
                                                    tmp3 = k.getTime();
                                                    lmp3 =j;
                                                }
                                            }
                                        }
                                        k = k.getNext();
                                    }
                                }
                            }
                        }
                    }
                }

                if(tmp2 <= tmp3){
                    if(lmp2 != numSpot - 1){
                        lmp1 = lmp2;
                        tmp1 = tmp2;
                    }
                    else{
                        lmp1 = lmp2;
                        tmp1 = tmp3;
                    }
                }
                else if(tmp2 > tmp3){
                    lmp1 = lmp3;
                    tmp1 = tmp3;
                }
            }

            else if(cnt == numSpot - 1){
                if(t.getLabel() == numSpot - 1){
                    tmp1 = t.getTime();
                    lmp1 = numSpot - 1;
                }
                else{
                    while(t.getNext() != null){
                        if(t.getNext().getLabel() == numSpot - 1){
                            tmp1 = t.getNext().getTime();
                            lmp1 = numSpot - 1;
                        }
                        t = t.getNext();
                    }
                }
            }

            pathLabel[i + 1] = lmp1;
            result = result + tmp1;
            visit.push(lmp1);
            cnt = cnt + 1;
        }

        for(int i = 0; i < numSpot - 1; i++){
            int l1 = pathLabel[i];
            int l2 = pathLabel[i+1];
            Spot p = list[l1];
            Spot q = new Spot();

            while(p != null){
                if(p.getLabel() == l2){
                    q = p;
                    break;
                }
                p = p.getNext();
            }

            path[i+1] = q;
        }

        //totTime = result;
        setPathway();
        return result;
    }

    /* 최단거리 자동차 비용 */
    public int totalCarCost(){
        int result = 0;

        for(int i = 1; i < numSpot; i++){
            result = result + path[i].getCostTaxi();
        }

        return 0;
    }

    /* 전체 경로 -> String으로 */
    public void setPathway(){
        pathway = path[0].getName();

        for(int i = 1; i < numSpot; i++) {
            pathway = pathway + " - " + path[i].getName();
        }
    }

    public String getPathway(){
        return pathway;
    }
}