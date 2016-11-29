'use strict';

describe('Controller Tests', function() {

    describe('Client Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockClient, MockChantier, MockAgenceClient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockClient = jasmine.createSpy('MockClient');
            MockChantier = jasmine.createSpy('MockChantier');
            MockAgenceClient = jasmine.createSpy('MockAgenceClient');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Client': MockClient,
                'Chantier': MockChantier,
                'AgenceClient': MockAgenceClient
            };
            createController = function() {
                $injector.get('$controller')("ClientDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gpbatApp:clientUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
