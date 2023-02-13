import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostService } from './services/post.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  form!: FormGroup;
  @ViewChild('file') // gets the HTML element directly
  uploadedPicture!: ElementRef;

  uploadStatus: string = 'not uploaded yet';

  constructor(private fb: FormBuilder, private postService: PostService) {}

  ngOnInit(): void {
    this.form = this.createForm();
  }

  createForm() {
    return this.fb.group({
      comments: this.fb.control('', [Validators.required]),
      picture: this.fb.control('', [Validators.required]),
    });
  }

  submitForm(): void {
    const comments = this.form.get('comments')?.value;
    const picture = this.uploadedPicture.nativeElement.files[0];

    // call post service to send POST request
    this.postService
      .createPost(comments, picture)
      .then((res) => {
        console.log(res);
        this.uploadStatus = 'success';
      })
      .catch((err) => {
        console.log(err);
        this.uploadStatus = 'error';
      });
  }
}
