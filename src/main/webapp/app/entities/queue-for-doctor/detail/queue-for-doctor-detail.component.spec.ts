import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QueueForDoctorDetailComponent } from './queue-for-doctor-detail.component';

describe('QueueForDoctor Management Detail Component', () => {
  let comp: QueueForDoctorDetailComponent;
  let fixture: ComponentFixture<QueueForDoctorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QueueForDoctorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ queueForDoctor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(QueueForDoctorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(QueueForDoctorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load queueForDoctor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.queueForDoctor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
