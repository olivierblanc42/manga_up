import { AbstractControl, ValidationErrors } from '@angular/forms';

export function noHtmlTagsValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (value && /<[^>]*>/g.test(value)) {
        return { noHtml: true };
    }
    return null;
}

export function urlValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null; 

    const pattern = /^https?:\/\/.+/i;
    return pattern.test(value) ? null : { invalidUrl: true };
  }


  export function requiredTrueValidator(control: AbstractControl) {
    return control.value === true ? null : { requiredTrue: true };
}
  