import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { MarvelCharacter } from '../models/MarvelCharacter.model';
import { CharacterService } from '../services/character.service';

@Component({
  selector: 'app-character-list',
  templateUrl: './character-list.component.html',
  styleUrls: ['./character-list.component.css'],
})
export class CharacterListComponent implements OnInit, OnDestroy {
  nameStartsWith!: string; // passed from search component
  routeSub$!: Subscription;
  currLimit: number = 10;
  currOffset: number = 0;
  characters: MarvelCharacter[] = [];

  constructor(
    private activateRoute: ActivatedRoute,
    private characterSvc: CharacterService
  ) {}

  ngOnInit(): void {
    // subscribe to changes in current route
    this.routeSub$ = this.activateRoute.queryParams.subscribe((params) => {
      // set nameStartsWith to current route query parameter
      this.nameStartsWith = params['nameStartsWith'];
      this.getCharacters();
    });
  }

  getCharacters() {
    // get list of characters starting with nameStartsWith
    this.characterSvc
      .getCharacters(this.nameStartsWith, this.currLimit, this.currOffset)
      .then((res) => {
        this.characters = res;
      })
      .catch((err) => {
        console.log(err);
      });
  }

  ngOnDestroy(): void {
    this.routeSub$.unsubscribe();
  }

  onChangePage(changeBy: number): void {
    // change offset and make new request to server
    this.currOffset = this.currOffset + this.currLimit * changeBy;
    this.getCharacters();
  }
}
