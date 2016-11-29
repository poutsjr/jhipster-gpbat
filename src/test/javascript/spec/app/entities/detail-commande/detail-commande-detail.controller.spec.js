'use strict';

describe('Controller Tests', function() {

    describe('DetailCommande Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDetailCommande, MockSimulation, MockCommande, MockBordereau;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDetailCommande = jasmine.createSpy('MockDetailCommande');
            MockSimulation = jasmine.createSpy('MockSimulation');
            MockCommande = jasmine.createSpy('MockCommande');
            MockBordereau = jasmine.createSpy('MockBordereau');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DetailCommande': MockDetailCommande,
                'Simulation': MockSimulation,
                'Commande': MockCommande,
                'Bordereau': MockBordereau
            };
            createController = function() {
                $injector.get('$controller')("DetailCommandeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gpbatApp:detailCommandeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
