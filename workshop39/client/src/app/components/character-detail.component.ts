import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { MarvelCharacter } from '../models/MarvelCharacter.model';
import { CharacterService } from '../services/character.service';

@Component({
  selector: 'app-character-detail',
  templateUrl: './character-detail.component.html',
  styleUrls: ['./character-detail.component.css'],
})
export class CharacterDetailComponent implements OnInit, OnDestroy {
  characterId!: number;
  routeSub$!: Subscription;
  character!: MarvelCharacter;

  constructor(
    private activatedRoute: ActivatedRoute,
    private characterSvc: CharacterService
  ) {}

  ngOnInit(): void {
    // get character id from current route
    this.routeSub$ = this.activatedRoute.params.subscribe((params) => {
      this.characterId = params['characterId'];
    });

    // retrieve character details from server
    this.characterSvc
      .getCharacterById(this.characterId)
      .then((res) => {
        this.character = res;
      })
      .catch((err) => {
        console.log(err);
      });
  }

  ngOnDestroy(): void {
    this.routeSub$.unsubscribe();
  }
}
