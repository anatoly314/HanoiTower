# HanoiTower

A simple GUI app to visualize how HanoiTower solving algorithm is working

![Preview](https://github.com/anatoly314/HanoiTower/blob/master/hanoitower.gif)


[Runnable jar can be downloaded from here](https://github.com/anatoly314/HanoiTower/releases/tag/0.1)

- **It can be invoked manually by adding all the steps**:

````
public static void main(String[] args) {
		HanoiGame game = new HanoiGame(4);
		game.addStep(0, 1);
		game.addStep(0, 2);
		game.addStep(1, 2);
		game.addStep(0, 1);
		game.addStep(2, 0);
		game.addStep(2, 1);
		game.addStep(0, 1);
		game.addStep(0, 2);
	}
````

- **Or it can be invoked by your own algorithm which which calculate required steps**:

````
    public static void main(String[] args) {
		HanoiGame game = new HanoiGame(4);
		solveHanoi(game, 4, 0, 1, 2);
	}
	
	public static void solveHanoi(HanoiGame game, int n, int start, int aux, int end){
		if(n == 1){
			game.addStep(start, end);
		}else{
			solveHanoi(game, n - 1, start, end, aux);
			game.addStep(start, end);
			solveHanoi(game, n - 1, aux, start, end);
		}
	}
````