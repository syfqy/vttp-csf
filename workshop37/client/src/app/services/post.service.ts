import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private httpClient: HttpClient) {}

  createPost(comments: string, picture: File) {
    // construct form data
    const formData = new FormData();
    formData.set('comments', comments);
    formData.set('picture', picture);

    // make POST request to server
    return lastValueFrom(this.httpClient.post('/api/post', formData));
  }
}
