import { TextService } from '@/core/http';
import { Injectable, OnInit } from '@angular/core';
import { map } from 'rxjs/operators';
import * as stopWordsJson from './stop-words-en.json';
import { HttpClient } from '@angular/common/http';


@Injectable({ providedIn: 'root' })
export class AnalysisService implements OnInit{

  stopWords: string[];

  constructor(private proxyService: TextService, private http: HttpClient) {}

  ngOnInit(){
  }

  analyse(url: string){
    var html = this.proxyService.getContent(url);

    return html.pipe(
      map(text => this.escapeHtmlTags(text)),
      map(escText => this.splitByWords(escText)),
      map(words => this.removeStopWords(words)),
      map(lastWords => this.createWordMap(lastWords))
    )
  }

  private escapeHtmlTags(html: string) {
    // html.replace(/<[^>]*>/g, "");
    return html.replace(/<head>(?:.|\n|\r)+?<\/head>/g, "") // remove head
      .replace(/<style(?:.|\n|\r)+?<\/style>/g, "") // remove style
      .replace(/<script(?:.|\n|\r)+?<\/script>/g, "") // remove script
      .replace(/<!--(?:.|\n|\r)+?-->/g, "") // remove comments 
      .replace(/<\/?[^>]+(>|$)/g, "") // remove all html tags
      .replace(/[^a-zA-Z0-9 ]/g, "") // remove all non alphabetic and numerical
      .replace(/\r?\n|\r/g, "") // remove all new lines
      .replace(/\s\s+/g, " ") // replace with one space all 2+ spaces
      .toLowerCase();
  }

  private splitByWords(text: string) {
    return text.split(/\s+/);
  }


  private removeStopWords(words: string[]){
    return this.difference(words, this.getStopWordsArray());
  }

  private getStopWordsArray(): Array<string> {
    return JSON.parse(JSON.stringify(stopWordsJson))
  }

  private difference(a1: string[], a2: string[]) {
    var result = [];
    for (var i = 0; i < a1.length; i++) {
      if (a2.indexOf(a1[i]) === -1) {
        result.push(a1[i]);
      }
    }
    return result;
  }

  private createWordMap(wordsArray: string[]) {
    var wordsMap = {};
    
    // fill the map with words and frequecy
    wordsArray.forEach(function (key) {
      if (wordsMap.hasOwnProperty(key)) {
        wordsMap[key]++;
      } else {
        wordsMap[key] = 1;
      }
    });

    var sorted = [];
    for (var key in wordsMap){
      if (wordsMap.hasOwnProperty(key)){
        sorted.push([key, wordsMap[key]]); // each item is an array in format [key, value]
      }
    }
    // sort items by value
    sorted.sort(function (a: any, b: any) {
      return b[1] - a[1]; // compare values
    });

    var sortedArray = [];
    for (var element of sorted){
      sortedArray.push(element[0]);
    }

    return sortedArray.slice(0, 10);
  }
}