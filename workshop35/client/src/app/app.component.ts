import { Component, OnInit } from '@angular/core';
import { MatSelectChange } from '@angular/material/select';
import { Game } from './models/game.model';
import { GameService } from './services/game.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  games: Game[] = [];
  limitOptions: number[] = [5, 10, 15, 20];
  currLimit: number = 10;
  currOffset: number = 0;

  // app component uses services to store results in games
  constructor(private gameService: GameService) {}

  // populate list of games when first loaded
  ngOnInit(): void {
    /*
    {
    "games": [
        {
            "gid": 13,
            "name": "Catan"
        }
    ],
    "offset": 5,
    "limit": 1,
    "total": 1,
    "timestamp": "2023-02-13 14:03:23.423"
}
    */

    this.gameService
      .getGames(this.currLimit, this.currOffset)
      .then((res) => {
        console.log(res);
        // assign array of games to app component's games
        this.games = res.games;
      })
      .catch((err) => {
        console.log(err);
      });
  }

  onLimitChange(newLimit: MatSelectChange) {
    // set new limit
    this.currLimit = newLimit.value;
    console.log('new limit change to : ' + this.currLimit);

    // make another request to serve
    this.ngOnInit();
  }

  onChangePage(changeBy: number) {
    // changeBy = -1 if prev page or 1 if next page
    // current page: 2, limit: 10, offset: 10 (rows 11-20)
    // prev page: 1, limit 10, offset: 10 + 10 * -1 = 0
    // next page: 3, limit 10, offset: 10 + 10 * 1 = 20
    this.currOffset = this.currOffset + this.currLimit * changeBy;

    // make another request to server
    this.ngOnInit();
  }
}
