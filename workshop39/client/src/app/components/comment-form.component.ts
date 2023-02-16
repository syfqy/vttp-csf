import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { CharacterService } from '../services/character.service';

@Component({
  selector: 'app-comment-form',
  templateUrl: './comment-form.component.html',
  styleUrls: ['./comment-form.component.css'],
})
export class CommentFormComponent implements OnInit, OnDestroy {
  form!: FormGroup;
  characterId!: number;
  routeSub$!: Subscription;
  characterName!: string;

  constructor(
    private fb: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private characterSvc: CharacterService
  ) {}

  ngOnInit(): void {
    // initialize the form
    this.clearForm();

    // get character id from current route
    this.routeSub$ = this.activatedRoute.params.subscribe((params) => {
      this.characterId = params['characterId'];
      // set character name based on character id
      this.setCharacterName();
    });
  }

  setCharacterName() {
    this.characterSvc
      .getCharacterById(this.characterId)
      .then((res) => {
        this.characterName = res.name;
      })
      .catch((err) => {
        console.log(err);
      });
  }

  createForm(): FormGroup {
    return this.fb.group({
      comment: this.fb.control('', [Validators.required]),
    });
  }

  clearForm(): void {
    this.form = this.createForm();
  }

  submitForm(): void {
    const comment = this.form.get('comment')?.value;

    // post comment to server
    this.characterSvc
      .postComment(this.characterId, comment)
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });

    // clear form
    this.clearForm();
  }

  ngOnDestroy(): void {
    this.routeSub$.unsubscribe();
  }
}
